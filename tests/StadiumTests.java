package tests;

import org.junit.Test;

import model.Case;
import model.Player;
import model.Stadium;
import model.enums.MoveDirection;
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

                // x = ligne et y = colonne
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
                    assertFalse(stadium.hasABall(new Case(x, y)));
                    assertFalse(stadium.hasAPlayerOnly(new Case(x, y)));
                }
            }
        }

        assertEquals(stadium.getNbMovesDone(), 0);
        assertEquals(stadium.getNbPassesDone(), 0);
    }

    @Test
    public void testCanMove() {
        Stadium stadium = new Stadium();

        for (Player player : stadium.getTeam(TeamPosition.TOP).getPlayers()) {
            assertFalse(player.canMove(MoveDirection.UP));

            if (player.hasBall()) {
                assertFalse(player.canMove(MoveDirection.DOWN));
            } else {
                assertTrue(player.canMove(MoveDirection.DOWN));
            }

            assertFalse(player.canMove(MoveDirection.LEFT));
            assertFalse(player.canMove(MoveDirection.RIGHT));
        }

        for (Player player : stadium.getTeam(TeamPosition.BOTTOM).getPlayers()) {
            if (player.hasBall()) {
                assertFalse(player.canMove(MoveDirection.UP));
            } else {
                assertTrue(player.canMove(MoveDirection.UP));
            }

            assertFalse(player.canMove(MoveDirection.DOWN));
            assertFalse(player.canMove(MoveDirection.LEFT));
            assertFalse(player.canMove(MoveDirection.RIGHT));
        }
    }

    @Test
    public void testCanPass() {
        Stadium stadium = new Stadium();

        Player pDiagonal = stadium.getPlayer(new Case(0, 0));
        stadium.move(pDiagonal, MoveDirection.DOWN);
        stadium.move(pDiagonal, MoveDirection.DOWN);
        stadium.move(pDiagonal, MoveDirection.DOWN);

        Player pVertical = stadium.getPlayer(new Case(0, 3));
        stadium.move(pVertical, MoveDirection.DOWN);
        stadium.move(pVertical, MoveDirection.RIGHT);

        Player pBall = stadium.getPlayer(new Case(0, 3));
        assertFalse(pBall.canPass(pBall));

        for (Player p : pBall.getTeam().getPlayers()) {
            if (p != pBall) {
                assertTrue(pBall.canPass(p));
            }
        }

        for (Player p : pBall.getTeam().getEnemyTeam().getPlayers()) {
            assertFalse(pBall.canPass(p));
        }

        Player pEnemy = stadium.getPlayer(new Case(6, 1));
        stadium.move(pEnemy, MoveDirection.UP);
        stadium.move(pEnemy, MoveDirection.UP);
        stadium.move(pEnemy, MoveDirection.UP);
        stadium.move(pEnemy, MoveDirection.UP);

        assertFalse("la passe aurait du etre bloquee", pBall.canPass(pDiagonal));
    }
}