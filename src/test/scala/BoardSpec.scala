import org.scalatest.FlatSpec

class BoardSpec extends FlatSpec {
  val characterList: Seq[GWCharacter] = GWCharacterList.characterList

  "a Board" should "have a seq field" in {
    val board: Board = new Board(characterList)
    assert(board.personSeq == GWCharacterList.characterList)
  }

  "updating a Board" should "take a new seq" in {
    val board: Board = new Board(characterList)
    val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")

    val updatedSeq: Seq[GWCharacter] = characterList ++ Seq(me)
    assert(board.updateBoard(updatedSeq).personSeq.length == updatedSeq.length)
  }

}
