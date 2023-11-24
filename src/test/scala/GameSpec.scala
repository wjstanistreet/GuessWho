
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers._
import org.scalatest.matchers.should.Matchers

class GameSpec extends AnyFlatSpec with Matchers {

  "Processing a Game" should "return a status code" in {
    val board: Board = new Board(GWCharacterList.characterList)
    val laraCroft: GWCharacter = GWCharacter("Lara Croft", "Female", 21,"long", "brown", "2 guns")

    val game: Game = new Game()
    game.process(laraCroft, GWCharacterList.characterList, new GameStatus(1, board)).getStatus should be (1)
  }
}
