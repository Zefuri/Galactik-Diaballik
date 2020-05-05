package tests;

import org.junit.Test;

import model.Case;
import model.Player;
import model.Stadium;
import model.enums.TeamPosition;

import static org.junit.Assert.*;

public class StadiumTests {

    @Test
    public void testInitialisation() {
        Stadium stadium = new Stadium();
        stadium.reset();

        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                Player player = stadium.getPlayer(new Case(x, y));

                // x et y semblent etre inversés
                if (x == 0 || x == 6) {
                    assertNotNull(player);
                    assertSame(player.getStadium(), stadium);
                    assertEquals(player.getPosition().getX(), x);
                    assertEquals(player.getPosition().getY(), y);

                    if (x == 0) {
                        assertSame(player.getTeam(), stadium.getTeam(TeamPosition.TOP));
                    } else {
                        assertSame(player.getTeam(), stadium.getTeam(TeamPosition.BOTTOM));
                    }

                    if (y == 3) {
                        assertTrue(player.hasBall());
                        assertTrue(stadium.hasABall(new Case(x, y)));
                        assertFalse(stadium.hasAPlayerOnly(new Case(x, y)));
                    } else {
                        assertFalse(player.hasBall());
                        assertFalse(stadium.hasABall(new Case(x, y)));
                        assertTrue(stadium.hasAPlayerOnly(new Case(x, y)));
                    }
                } else {
                    assertNull(player);
                }
            }
        }

        assertEquals(stadium.getNbMovesDone(), 0);
        assertEquals(stadium.getNbPassesDone(), 0);
    }
}