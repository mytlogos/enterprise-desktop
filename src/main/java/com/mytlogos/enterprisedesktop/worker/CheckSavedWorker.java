package com.mytlogos.enterprisedesktop.worker;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.tools.ContentTool;
import com.mytlogos.enterprisedesktop.tools.FileTools;
import javafx.concurrent.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class CheckSavedWorker extends Task<Void> {

    @Override
    protected Void call() {
        this.updateTitle("Check Saved");
        Set<ContentTool> tools = FileTools.getSupportedContentTools();
        Repository repository = ApplicationConfig.getRepository();

        this.updateMessage("Checking for saved Episodes");
        Set<Integer> savedEpisodes = new HashSet<>();

        for (ContentTool tool : tools) {
            savedEpisodes.addAll(this.putItemContainer(tool));
        }
        final List<Integer> previouslySavedEpisodes = repository.getSavedEpisodes();
        Set<Integer> unsavedEpisodes = new HashSet<>(previouslySavedEpisodes);
        // get all the unsaved episodes
        unsavedEpisodes.removeAll(savedEpisodes);
        // get the newly saved episodes, not visible from database
        savedEpisodes.removeAll(previouslySavedEpisodes);

        this.updateMessage("Persisting");
        repository.updateSaved(unsavedEpisodes, false);
        repository.updateSaved(savedEpisodes, true);
        this.updateMessage("Finished");
        return null;
    }

    private Set<Integer> putItemContainer(ContentTool bookTool) {
        final List<String> mediaPaths = bookTool.getMediaPaths();
        Set<Integer> savedEpisodes = new HashSet<>();

        for (String mediaPath : mediaPaths) {
            final Map<Integer, String> episodePaths = bookTool.getEpisodePaths(mediaPath);
            savedEpisodes.addAll(episodePaths.keySet());
        }
        return savedEpisodes;
    }
}
