
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers._
import org.scalatest.matchers.should.Matchers

class GameSpec extends AnyFlatSpec with Matchers {

  "Processing a Game" should "return a status code" in {
    val board: Board = new Board(GWCharacterList.characterList)
    val me: GWCharacter = GWCharacter("Will Stanistreet", "Male", 24, "medium", "brown", "a blue jumper")

    val game: Game = new Game()
    game.process(me, GWCharacterList.characterList, new GameStatus(1, board)).getStatus should be (1)
  }

  "putting a character down from a guess" should "remove them from the sequence" in {
    val name: String = "will stanistreet"

    val updatedList: Seq[GWCharacter] = Seq(GWCharacter("Omiros Trypatsas", "Male", 24, "short", "black", "a blue jumper"),
      GWCharacter("Aashvin Relwani", "Male", 24, "short", "brown", "a seasac shirt"),
      GWCharacter("Sarina Salomon", "Female", 24, "long", "black", "black boots"),
      GWCharacter("Connie Bernardin", "Female", 23, "medium", "blonde", "a gillet"))

    val game: Game = new Game()
    game.putCharacterDownGuess(name, GWCharacterList.characterList, new Board(GWCharacterList.characterList)).personSeq.length should be (updatedList.length)
  }
}
