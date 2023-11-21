import scala.io.StdIn

class Board(val personSeq: Seq[GWCharacter]) {

  def printRemainingCharacters(): Unit = {
    println("The characters on the board are:")
    personSeq.foreach((person) => println(s"A ${person.gender}, aged ${person.age}, with ${person.hairLength}, ${person.hairColour} hair. They have ${person.accessory} and their name is ${person.name}."))
  }

  def updateBoard(newSequence: Seq[GWCharacter]): Board = new Board(newSequence)
}
