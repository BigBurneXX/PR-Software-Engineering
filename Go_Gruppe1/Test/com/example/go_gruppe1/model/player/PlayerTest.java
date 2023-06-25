package com.example.go_gruppe1.model.player;
import javafx.scene.paint.Color;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PlayerTest {

    private Player player;
    private Player player2;

    @Before
    public void setUp() {
        player = new Player("John", Color.BLACK, Color.GRAY, 3, 10);
        player2 = new Player("Martin", Color.BLUE, Color.BLUEVIOLET, 0, 0);
    }

    @Test
    public void testGetName() {
        assertEquals("John", player.getName());
        assertEquals("Martin", player2.getName());
    }

    @Test
    public void testGetColor() {
        assertEquals(Color.BLACK, player.getColor());
        assertEquals(Color.BLUE, player2.getColor());
    }

    @Test
    public void testGetHoverColor() {
        assertEquals(Color.GRAY, player.getHoverColor());
        assertEquals(Color.BLUEVIOLET, player2.getHoverColor());
    }

    @Test
    public void testGetTimer() {
        assertNotNull(player.getTimer());
        assertNull(player2.getTimer());
    }

    @Test
    public void testGetTimeLabelText() {
        assertNotNull(player.getTimeLabelText());
        assertNull(player2.getTimeLabelText());
    }

    @Test
    public void testGetByoyomi() {
        assertEquals(3, player.getByoyomi());
        assertEquals(0, player2.getByoyomi());
    }

    @Test
    public void testSetByoyomi() {
        player.setByoyomi(5);
        assertEquals(5, player.getByoyomi());
    }

    @Test
    public void testSetTimeLabelText() {
        String expectedLabelText = "10 seconds remaining";
        player.setTimeLabelText(expectedLabelText);
        assertNotNull(player.getTimeLabelText());
        assertEquals(expectedLabelText, player.getTimeLabelText().get());
    }
}