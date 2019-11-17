package com.mytlogos.enterprisedesktop.tools;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.api.model.ClientDownloadedEpisode;
import com.mytlogos.enterprisedesktop.model.ChapterPage;
import com.mytlogos.enterprisedesktop.model.MediumType;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageContentTool extends ContentTool {
    private Map<Integer, File> imageMedia;

    ImageContentTool(File contentDir) {
        super(contentDir);
    }

    @Override
    public int getMedium() {
        return MediumType.IMAGE;
    }

    boolean isContentMedium(File file) {
        return file.getName().matches("\\d+") && file.isDirectory();
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    void removeMediaEpisodes(Set<Integer> episodeIds, String path) {
        if (path == null) {
            return;
        }
        File file = new File(path);
        Set<String> prefixes = new HashSet<>();

        for (Integer episodeId : episodeIds) {
            prefixes.add(episodeId + "-");
        }
        final File[] files = file.listFiles();
        if (files == null) {
            System.err.println("could not files of: " + file);
            return;
        }
        for (File episodePath : files) {

            String name = episodePath.getName();

            for (String prefix : prefixes) {
                if (!name.startsWith(prefix)) {
                    continue;
                }
                try {
                    if (episodePath.exists() && !episodePath.delete()) {
                        String idSubString = prefix.substring(0, prefix.indexOf("-"));
                        System.err.printf("could not delete episode %s totally, deleting: '%s' failed%n", idSubString, file.getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    Pattern getMediumContainerPattern() {
        return Pattern.compile("^(\\d+)$");
    }

    @Override
    int getMediumContainerPatternGroup() {
        return 1;
    }

    @Override
    public Map<Integer, String> getEpisodePaths(String mediumPath) {
        File file = new File(mediumPath);
        if (!file.exists() || !file.isDirectory()) {
            return Collections.emptyMap();
        }
        Pattern pagePattern = Pattern.compile("^(\\d+)-\\d+\\.(png|jpg)$");


        Map<Integer, String> firstPageEpisodes = new HashMap<>();

        final String[] files = file.list();

        if (files == null) {
            System.err.println("could not files of: " + file);
            return firstPageEpisodes;
        }
        for (String episodePath : files) {
            Matcher matcher = pagePattern.matcher(episodePath);

            if (!matcher.matches()) {
                continue;
            }
            String episode = matcher.group(1);
            int episodeId = Integer.parseInt(episode);

            // look for available pages
            if (!firstPageEpisodes.containsKey(episodeId)) {
                firstPageEpisodes.put(episodeId, episodePath);
            }
        }
        return firstPageEpisodes;
    }

    @Override
    String getItemPath(int mediumId, File dir) {
        final File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("could not files of: " + dir);
            return null;
        }
        for (File file : files) {
            if ((mediumId + "").equals(file.getName()) && file.isDirectory()) {
                return file.getAbsolutePath();
            }
        }
        return null;
    }

    @Override
    public void saveContent(Collection<ClientDownloadedEpisode> episodes, int mediumId) throws IOException {
        if (this.imageMedia == null) {
            this.imageMedia = this.getItemContainers();
        }
        File file;

        if (!this.writeable()) {
            throw new NotEnoughSpaceException("Out of Storage Space: Less than " + minMBSpaceAvailable + " MB available");
        }

        if (this.imageMedia.containsKey(mediumId)) {
            file = this.imageMedia.get(mediumId);
        } else {
            file = new File(this.contentDir, mediumId + "");

            if (!file.exists() && !file.mkdir()) {
                throw new IOException("could not create image medium directory");
            }
        }
        for (ClientDownloadedEpisode episode : episodes) {
            String[] content = episode.getContent();
            if (content == null || content.length == 0) {
                continue;
            }
            List<String> links = ApplicationConfig.getRepository().getReleaseLinks(episode.getEpisodeId());
            List<File> writtenFiles = new ArrayList<>();

            downloadPage(content, 0, file, episode.getEpisodeId(), links, writtenFiles);

            File firstImage = writtenFiles.get(0);
            long estimatedByteSize = firstImage.length() * content.length;

            if (notWriteable(file, estimatedByteSize)) {
                if (firstImage.exists() && !firstImage.delete()) {
                    System.out.println("could not delete image: " + firstImage.getAbsolutePath());
                }
                throw new NotEnoughSpaceException();
            }

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int page = 1, contentLength = content.length; page < contentLength; page++) {
                int tempPage = page;
                futures.add(CompletableFuture.runAsync(() -> {
                    try {
                        downloadPage(content, tempPage, file, episode.getEpisodeId(), links, writtenFiles);
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }));
            }
            try {
                CompletableFuture
                        .allOf(futures.toArray(new CompletableFuture[0]))
                        .whenComplete((aVoid, throwable) -> {
                            if (throwable == null) {
                                return;
                            }
                            // first exception is completionException, then the illegalStateException
                            // followed by notEnoughSpaceException
                            Throwable cause = throwable.getCause();
                            if (cause != null && cause.getCause() instanceof NotEnoughSpaceException) {
                                cause = cause.getCause();
                                for (File writtenFile : writtenFiles) {
                                    if (writtenFile.exists() && !writtenFile.delete()) {
                                        System.out.println("could not delete image: " + writtenFile.getAbsolutePath());
                                    }
                                }
                                throwable = cause.getCause();
                            }
                            throw new RuntimeException(throwable);
                        })
                        .get();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof NotEnoughSpaceException) {
                    throw new NotEnoughSpaceException();
                } else if (e.getCause() instanceof IOException) {
                    throw new IOException(e);
                } else {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadPage(String[] content, int page, File file, int episodeId, List<String> links, List<File> writtenFiles) throws IOException {
        String link = content[page];
        String pageLinkDomain = Utils.getDomain(link);

        if (pageLinkDomain == null) {
            System.err.println("invalid url: '" + link + "'");
            return;
        }
        String referer = null;

        for (String releaseUrl : links) {
            if (Objects.equals(Utils.getDomain(link), pageLinkDomain)) {
                referer = releaseUrl;
                break;
            }
        }
        if (referer == null || referer.isEmpty()) {
            // we need a referrer for sites like mangahasu
            return;
        }
        // TODO: 06.08.2019 instead of continuing maybe create an empty image file to signal
        //  the reader that this page is explicitly missing?
        if (link == null || link.isEmpty()) {
            System.err.println("got an invalid link");
            return;
        }
        String imageFormat;

        if (link.toLowerCase().endsWith(".png")) {
            imageFormat = "png";
        } else if (link.toLowerCase().endsWith(".jpg")) {
            imageFormat = "jpg";
        } else {
            System.err.println("got unsupported/unwanted image format: " + link);
            return;
        }
        try {
            URL url = new URL(link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Referer", referer);
            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("invalid response for " + link);
                return;
            }

            try (BufferedInputStream in = new BufferedInputStream(httpURLConnection.getInputStream())) {
                String pageName = String.format("%s-%s.%s", episodeId, page + 1, imageFormat);
                File image = new File(file, pageName);
                writtenFiles.add(image);

                saveImageStream(in, image);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // if the estimation was too low
        // and subsequent images took more space than expected
        // check if it can still write after this
        if (!this.writeable()) {
            throw new NotEnoughSpaceException();
        }
    }

    private void saveImageStream(InputStream in, File image) throws IOException {
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(image))) {
            int ch;
            while ((ch = in.read()) != -1) {
                outputStream.write(ch);
            }
        }
    }

    private void saveImageBitmap(InputStream in, File image) throws IOException {
        throw new UnsupportedOperationException();
//        Bitmap bitmap = BitmapFactory.decodeStream(in);
//
//        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(image))) {
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
//            outputStream.flush();
//        }
    }

    @Override
    public long getEpisodeSize(File value, int episodeId) {
        String prefix = episodeId + "-";
        long size = 0;

        final File[] files = value.listFiles();
        if (files == null) {
            System.err.println("could not files of: " + value);
            return size;
        }
        for (File file : files) {
            if (!file.getName().startsWith(prefix)) {
                continue;
            }
            size += file.length();
        }
        return size;
    }

    @Override
    public double getAverageEpisodeSize(int mediumId) {
        String itemPath = this.getItemPath(mediumId);
        double sum = 0;

        File[] files = new File(itemPath).listFiles();
        if (files == null) {
            System.err.println("could not files of: " + itemPath);
            return sum;
        }
        for (File file : files) {
            sum += file.length();
        }
        return files.length == 0 ? 0 : sum / files.length;
    }

    public Map<Integer, Set<ChapterPage>> getEpisodePagePaths(String mediumDir) {
        File file = new File(mediumDir);

        if (!file.exists() || !file.isDirectory()) {
            return Collections.emptyMap();
        }
        Pattern pagePattern = Pattern.compile("^(\\d+)-(\\d+)\\.(png|jpg)$");


        Map<Integer, Set<ChapterPage>> episodePages = new HashMap<>();

        final String[] files = file.list();

        if (files == null) {
            System.err.println("could not files of: " + file);
            return episodePages;
        }
        for (String episodePath : files) {
            Matcher matcher = pagePattern.matcher(episodePath);

            if (!matcher.matches()) {
                continue;
            }

            String episodeIdString = matcher.group(1);
            String pageString = matcher.group(2);
            int episodeId = Integer.parseInt(episodeIdString);
            int page = Integer.parseInt(pageString);

            String absolutePath = new File(file, episodePath).getAbsolutePath();

            episodePages
                    .computeIfAbsent(episodeId, integer -> new HashSet<>())
                    .add(new ChapterPage(episodeId, page, absolutePath));
        }
        return episodePages;
    }
}
