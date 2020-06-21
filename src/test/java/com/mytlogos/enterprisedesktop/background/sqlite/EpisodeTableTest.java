package com.mytlogos.enterprisedesktop.background.sqlite;

import com.mytlogos.enterprisedesktop.background.api.model.ClientPart;
import com.mytlogos.enterprisedesktop.model.EpisodeImpl;
import com.mytlogos.enterprisedesktop.model.SimpleMedium;
import com.mytlogos.enterprisedesktop.model.SimpleRelease;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

/**
 *
 */
class EpisodeTableTest {

    private final TestConnectionManager manager;
    private EpisodeTable episodeTable;
    private MediumTable mediumTable;
    private PartTable partTable;
    private ReleaseTable releaseTable;

    EpisodeTableTest() {
        this.manager = new TestConnectionManager();
    }

    @BeforeEach
    void setUp() {
        this.episodeTable = new EpisodeTable(this.manager);
        this.episodeTable.initialize();
        this.mediumTable = new MediumTable(this.manager);
        this.mediumTable.initialize();
        this.partTable = new PartTable(this.manager);
        this.partTable.initialize();
        this.releaseTable = new ReleaseTable(this.manager);
        this.releaseTable.initialize();
    }

    @AfterEach
    void tearDown() {
        this.episodeTable.deleteAll();
        this.mediumTable.deleteAll();
        this.partTable.deleteAll();
        this.releaseTable.deleteAll();
    }

    @Test
    void initialize() {
        try {
            try (Statement statement = this.manager.getConnection().createStatement()) {
                statement.execute("DROP TABLE IF EXISTS episode");
            }
            try (Statement statement = this.manager.getConnection().createStatement()) {
                final ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='episode';");
                Assertions.assertFalse(resultSet.next());
            }
            this.episodeTable.initialize();

            try (Statement statement = this.manager.getConnection().createStatement()) {
                final ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='episode';");
                Assertions.assertTrue(resultSet.next());
            }
        } catch (SQLException e) {
            Assertions.fail(e);
        }
    }

    @Test
    void updateProgress() {
    }

    @Test
    void testUpdateProgress() {
    }

    @Test
    void getDownloadable() {
    }

    @Test
    void getSimpleEpisode() {
    }

    @Test
    void getSimpleEpisodes() {
    }

    @Test
    void updateSaved() {
    }

    @Test
    void testUpdateSaved() {
    }

    @Test
    void getSavedEpisodes() {
    }

    @Test
    void getStat() {
    }

    @Test
    void getEpisodes() {
    }

    @Test
    void deletePerId() {
        mediumTable.insert(new SimpleMedium(1, "N/A", 1));

        partTable.insert(new ClientPart(1, 1, "", 0, 0, null));

        this.episodeTable.deleteAll();
        Set<Integer> loadedInt = this.episodeTable.getLoadedInt();
        Assertions.assertTrue(loadedInt.isEmpty());

        this.episodeTable.insert(new EpisodeImpl(1, 0, 1, 0, 0, 0, null, true));
        this.episodeTable.insert(new EpisodeImpl(2, 0, 1, 0, 0, 0, null, true));

        loadedInt = this.episodeTable.getLoadedInt();
        Assertions.assertEquals(loadedInt.size(), 2);

        releaseTable.deleteAll();

        Assertions.assertEquals(releaseTable.getLinks(1).size(), 0);
        Assertions.assertEquals(releaseTable.getLinks(2).size(), 0);

        releaseTable.insert(new SimpleRelease(1, "", "", false, LocalDateTime.now()));
        releaseTable.insert(new SimpleRelease(2, "", "", false, LocalDateTime.now()));

        Assertions.assertEquals(releaseTable.getLinks(1).size(), 1);
        Assertions.assertEquals(releaseTable.getLinks(2).size(), 1);

        this.episodeTable.deletePerId(Collections.singletonList(1));

        loadedInt = this.episodeTable.getLoadedInt();
        Assertions.assertEquals(loadedInt.size(), 1);
        Assertions.assertTrue(loadedInt.contains(2));

        Assertions.assertEquals(releaseTable.getLinks(1).size(), 0);
        Assertions.assertEquals(releaseTable.getLinks(2).size(), 1);
    }

    @Test
    void getReleases() {
    }

    @Test
    void update() {
    }

    @Test
    void getMediumEpisodeIds() {
    }

    @Test
    void removeMediumEpisodes() {
    }

    @Test
    void getMediumEpisodes() {
    }

    @Test
    void getToc() {
    }

    @Test
    void insert() {
    }
}