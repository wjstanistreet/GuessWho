import org.scalatest.flatspec._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers._
import org.scalatest.matchers.should.Matchers
//import org.scalatest.matchers.should

class GameStatusSpec extends AnyFlatSpec with BeforeAndAfterEach with Matchers{

  var gameStatus: GameStatus = null

  override def beforeEach(): Unit = {
    super.beforeEach()
    gameStatus = new GameStatus(0, new Board(GWCharacterList.characterList))
  }

  "A Game Status code" should "be get-able" in {
    gameStatus.getStatus should equal(0)
  }

  "Updating a Game Status" should "change it's code" in {
    gameStatus.updateStatusNum(1).getStatus should equal(1)
  }

  "A Game Status board" should "be get-able" in {
    gameStatus.getBoard.personSeq should equal(new Board(GWCharacterList.characterList).personSeq)
  }

  "Updating a GameStatus board" should "change it's code" in {
    val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
    val updatedSeq: Seq[GWCharacter] = GWCharacterList.characterList ++ Seq(me)
    val board: Board = new Board(updatedSeq)

    gameStatus.updateStatusBoard(board).getBoard.personSeq.length should equal(5)
  }

}
