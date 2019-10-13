package com.mytlogos.enterprisedesktop.background;

import com.mytlogos.enterprisedesktop.background.resourceLoader.DependencyTask;
import com.mytlogos.enterprisedesktop.background.resourceLoader.LoadWorkGenerator;

import java.util.Collection;

public interface DependantGenerator {

    Collection<DependencyTask<?>> generateReadEpisodesDependant(LoadWorkGenerator.FilteredReadEpisodes readEpisodes);

    Collection<DependencyTask<?>> generatePartsDependant(LoadWorkGenerator.FilteredParts parts);

    Collection<DependencyTask<?>> generateEpisodesDependant(LoadWorkGenerator.FilteredEpisodes episodes);

    Collection<DependencyTask<?>> generateMediaDependant(LoadWorkGenerator.FilteredMedia media);

    Collection<DependencyTask<?>> generateMediaListsDependant(LoadWorkGenerator.FilteredMediaList mediaLists);

    Collection<DependencyTask<?>> generateExternalMediaListsDependant(LoadWorkGenerator.FilteredExtMediaList externalMediaLists);

    Collection<DependencyTask<?>> generateExternalUsersDependant(LoadWorkGenerator.FilteredExternalUser externalUsers);
}
