package com.mytlogos.enterprisedesktop.test;


import com.comcast.viper.hlsparserj.*;
import com.comcast.viper.hlsparserj.tags.master.StreamInf;
import com.comcast.viper.hlsparserj.tags.media.ExtInf;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class SimpleTest {
    public static void main(String[] args) throws Exception {
        URL uri = new URL("https://node8.vidstreampng.xyz");
        final URL url = new URL("https://node8.vidstreampng.xyz/hls/ea5660b2b875bb08bf5f9c1d297ebe31/ea5660b2b875bb08bf5f9c1d297ebe31.playlist.m3u8");
        final URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0");
        System.out.println(url);
        final AbstractPlaylist abstractPlaylist = PlaylistFactory.parsePlaylist(PlaylistVersion.DEFAULT, urlConnection.getInputStream());
        if (abstractPlaylist.isMasterPlaylist()) {
            MasterPlaylist masterPlaylist = (MasterPlaylist) abstractPlaylist;

            URL topBitrateVariant = null;
            int highestBitrate = Integer.MIN_VALUE;
            for (StreamInf variant : masterPlaylist.getVariantStreams()) {
                if (variant.getBandwidth() > highestBitrate) {
                    topBitrateVariant = new URL(uri, variant.getURI());
                    highestBitrate = variant.getBandwidth();
                }
            }
            System.out.println(highestBitrate);
            System.out.println(topBitrateVariant);

            final URLConnection connection = topBitrateVariant.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0");
            final AbstractPlaylist subPlayList = PlaylistFactory.parsePlaylist(PlaylistVersion.DEFAULT, connection.getInputStream());

            System.out.println(subPlayList);
            MediaPlaylist mediaPlaylist = (MediaPlaylist) subPlayList;
            final List<ExtInf> segments = mediaPlaylist.getSegments();

            final File file = new File("test.mp4");
//            try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
//                for (ExtInf segment : segments) {
//                    System.out.printf("Title: %s, Uri: %s%n", segment.getTitle(), segment.getURI());
//                    final URLConnection segmentConnection = new URL(segment.getURI()).openConnection();
//                    segmentConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:70.0) Gecko/20100101 Firefox/70.0");
//                    try (BufferedInputStream stream = new BufferedInputStream(segmentConnection.getInputStream())) {
//                        byte[] buffer = new byte[1024];
//                        int len = stream.read(buffer);
//
//                        while (len != -1) {
//                            outputStream.write(buffer, 0, len);
//                            len = stream.read(buffer);
//                        }
//                    }
//                }
//            }
            for (ExtInf segment : segments) {
                System.out.printf("Uri: %s, Duration. %f, Datetime: %s, Discontinuity: %s, Key: %s%n", segment.getURI(), segment.getDuration(), segment.getDateTime(), segment.getDiscontinuity(), segment.getKey());
            }
        }

        System.out.println(abstractPlaylist);
    }

    private static void d(Iterable<Integer> integers) {

    }

    private static class A<T> {
        List<T> value;

        void hello(List<T> list) {
            for (T t : list) {
                System.out.println(t);
            }
        }
    }

    private static class B extends A {
    }

}
