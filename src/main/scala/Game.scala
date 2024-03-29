import scala.util.Random
import scala.io.StdIn

class Game {

  /**
   * Starts the game
   */
  def start(): Unit = {
    // Introduction
    println("~~~ Welcome to Guess Who! ~~~")
    printInstructions()

    // Initialise the characters and the board
    val characterCollection = GWCharacterList.characterList
    val randomCharacter     = selectRandomCharacter(characterCollection)
    val board               = new Board(characterCollection)

    running(randomCharacter, characterCollection, board)
}

  /**
   * Method to run the Guess Who processing
   * @param selectedCharacter The randomly selected mystery character
   * @param characterSeq Sequence of characters to chose from
   * @param board Current state of the board
   */
  def running(selectedCharacter: GWCharacter, characterSeq: Seq[GWCharacter], board: Board): Unit = {
    // Displays the starting board state and starts prompting the user for guesses
    // Once the (process) call stack is empty, end the game
    board.printRemainingCharacters()
    process(selectedCharacter, characterSeq, new GameStatus(0, board))
    endGame()
  }

  /**
   * Prompts the user to select if they want to guess who the mystery character is,
   * or ask a question to reduce the number of characters that are up
   * @param selectedCharacter The randomly selected mystery character
   * @param board The current board state
   * @param status Status for the game
   * @return An updated game status
   */
  def process(selectedCharacter: GWCharacter, characterSeq: Seq[GWCharacter], status: GameStatus): GameStatus = {
    // Status Code 0: Prompt the user to guess or question the characters on the board
    status.getStatus match {
      case 0 => processGuessQuestionPrompt(selectedCharacter, characterSeq, status)
      case 1 => status.updateStatusNum(1)
      case 2 => printInstructions(); process(selectedCharacter, characterSeq, status.updateStatusNum(0))
      case _ => process(selectedCharacter, characterSeq, status.updateStatusNum(0))
    }
  }

  private def processGuessQuestionPrompt(selectedCharacter: GWCharacter, characterSeq: Seq[GWCharacter], status: GameStatus) = {
    println("Do you want to make a guess or ask a question?")
    val guessOrQ: String = StdIn.readLine().toLowerCase.trim

    if (guessOrQ == "help") process(selectedCharacter, characterSeq, status.updateStatusNum(2))

    lazy val processGuessVal    = processGuess(selectedCharacter, characterSeq, status)
    lazy val processQuestionVal = processQuestion(selectedCharacter, characterSeq, status)

    guessOrQ match {
      case "g"        => process(selectedCharacter, processGuessVal.getBoard.personSeq, processGuessVal)
      case "guess"    => process(selectedCharacter, processGuessVal.getBoard.personSeq, processGuessVal)
      case "q"        => process(selectedCharacter, processQuestionVal.getBoard.personSeq, processQuestionVal)
      case "question" => process(selectedCharacter, processQuestionVal.getBoard.personSeq, processQuestionVal)
      case _          => process(selectedCharacter, characterSeq, status.updateStatusNum(0))
    }
  }

  /**
   * Prompts the user to guess who the mystery character is. If the user guesses correctly,
   * processGuess returns true
   *
   * @param selectedCharacter - The randomly selected mystery character for the game
   * @param board - The current board state
   * @return An updated game status
   */
  def processGuess(selectedCharacter: GWCharacter, characterSeq: Seq[GWCharacter], status: GameStatus): GameStatus = {
    println("Who do you think the mystery character is?")
    val guess: String = StdIn.readLine().toLowerCase.trim

    if (guess == "help") return status.updateStatusNum(2)

    if (guess.toLowerCase != selectedCharacter.name.toLowerCase) {
      println("That's not quite right... Try again")
      new GameStatus(0, putCharacterDownGuess(guess, characterSeq, status.getBoard))

    } else status.updateStatusNum(1)
  }

  /**
   * Prompts the user to choose a question and processes their answer
   * @param board The current board state
   * @return An updated board state
   */
  def processQuestion(selectedCharacter: GWCharacter, characterSeq: Seq[GWCharacter], status: GameStatus): GameStatus = {
    println("What attribute would you like to guess for?")
    println("1. Gender      - \"Is the character male or female?\" \n" +
            "2. Age         - \"How old is the character?\" \n" +
            "3. Hair Colour - \"Does the character have black, brown, blonde, or red hair?\" \n" +
            "4. Hair Length - \"Does the character have short or long hair?\" \n" +
            "5. Accessory   - \"What accessory does the character have?\"")
    val attribute: String = StdIn.readLine().toLowerCase.trim

    if (attribute == "help") return status.updateStatusNum(2)

    val updatedBoard: Board = putCharacterDownQuestion(attribute, selectedCharacter, characterSeq)

    status.updateStatusBoard(updatedBoard)
  }

  /**
   * Ends the game
   */
  def endGame(): Unit = {
    println("Congratulations, you guessed it correctly! Well done! \nDo you want to play again?")
    val input = StdIn.readLine().toLowerCase.trim
    if (input == "yes" || input == "y") start()
  }

  /**
   * Displays the Guess Who instructions to the console
   */
  def printInstructions(): Unit = {
    println("How to play: \nA mystery character is selected at random. Using yes or no questions, try to figure out who the mystery character is.\n" +
      "When you think you know who the mystery character is, make a guess! \n" +
      "If you need these instructions again, type 'help'. Good luck!")
  }

  /**
   * Chooses a random character from a sequence of characters
   * @param characterCollection A sequence of characters to select at random
   * @return A randomly selected character
   */
  def selectRandomCharacter(characterCollection: Seq[GWCharacter]): GWCharacter = {
    println(s"A random character has been chosen from a list of ${characterCollection.length}.")
    characterCollection(Random.nextInt(characterCollection.length))
  }

  /**
   * Puts characters down depending on the attribute chosen
   * @param attribute Selected character attribute
   * @param selectedCharacter Selected random character to compare attributes
   * @param characterSeq Sequence of characters to compare
   * @return Updated board state
   */
  def putCharacterDownQuestion(attribute: String, selectedCharacter: GWCharacter, characterSeq: Seq[GWCharacter]): Board = {
    // TODO: REFACTOR FOR TESTING
    // Create a filtered sequence from
    val filteredSeq: Seq[GWCharacter] = attribute match {
      case "gender"       | "1" => genderGuess(selectedCharacter, characterSeq)
      case "age"          | "2" => ageGuess(selectedCharacter, characterSeq)
      case "hair colour"  | "3" => hairColourGuess(selectedCharacter, characterSeq)
      case "hair length"  | "4" => hairLengthGuess(selectedCharacter, characterSeq)
      case "accessory"    | "5" => accessoryGuess(selectedCharacter, characterSeq)
      case _ => println("Attribute not recognised, try again..."); characterSeq
    }

    val newBoard: Board = new Board(filteredSeq)
    newBoard.printRemainingCharacters()
    newBoard
  }

  /**
   * Puts characters down depending on the
   * @param name Guessed name
   * @param characterSeq Selected random character to compare attributes
   * @param board Board to update
   * @return Updated board state
   */
  def putCharacterDownGuess(name: String, characterSeq: Seq[GWCharacter], board: Board): Board = {
    val filteredSeq: Seq[GWCharacter] = characterSeq.filter(character => character.name.toLowerCase != name)

    val newBoard: Board = board.updateBoard(filteredSeq)
    newBoard.printRemainingCharacters()
    newBoard
  }

  /**
   * Prompts the user to guess the character's gender
   * @param randomCharacter Selected character to compare attribute
   * @param characterSeq Character sequence to filter
   * @return Reduced character sequence to continue the game
   */
  def genderGuess(randomCharacter: GWCharacter, characterSeq: Seq[GWCharacter]): Seq[GWCharacter] = {
    // TODO: REFACTOR FOR TESTING
    println("Is the character male or female?")
    val guess: String = StdIn.readLine().toLowerCase.trim
    val newCharacterSeq: Seq[GWCharacter] =
      if (randomCharacter.gender.toLowerCase == guess) {
        println(s"Yes! The mystery character is ${randomCharacter.gender}!")
        characterSeq.filter(character => character.gender.toLowerCase == guess)
      } else {
        println(s"No... the mystery character is ${randomCharacter.gender}.")
        characterSeq.filter(character => character.gender.toLowerCase != guess)
      }

    println(s"You put down ${characterSeq.length - newCharacterSeq.length} people")
    newCharacterSeq
  }

  /**
   * Prompts the user to guess the character's age
   * @param randomCharacter Selected character to compare attribute
   * @param characterSeq Character sequence to filter
   * @return Reduced character sequence to continue the game
   */
  def ageGuess(randomCharacter: GWCharacter, characterSeq: Seq[GWCharacter]): Seq[GWCharacter] = {
    // TODO: REFACTOR FOR TESTING
    println("How old is the character? Please enter a numeral, e.g: 1, 18, 43")
      val guess: String = StdIn.readLine().toLowerCase.trim
    try {
      val newCharacterSeq: Seq[GWCharacter] =
        if (randomCharacter.age == guess.toInt) {
          println(s"Yes! The mystery character is ${randomCharacter.age} years old!")
          characterSeq.filter(character => character.age.toString == guess)
        } else {
          println(s"No... the mystery character isn't $guess years old.")
          characterSeq.filter(character => character.age.toString != guess)
        }
      println(s"You put down ${characterSeq.length - newCharacterSeq.length} people")
      newCharacterSeq
    } catch {
      case nonNumeral: NumberFormatException => {
        println("You've entered a number name, please enter a numeral, e.g: 1, 18, 43 ")
        characterSeq
      }
    }
  }

  /**
   * Prompts the user to guess the character's hair colour
   * @param randomCharacter Selected character to compare attribute
   * @param characterSeq Character sequence to filter
   * @return Reduced character sequence to continue the game
   */
  def hairColourGuess(randomCharacter: GWCharacter, characterSeq: Seq[GWCharacter]): Seq[GWCharacter] = {
    // TODO: REFACTOR FOR TESTING
    println("Does the character have black, brown, blonde, or red hair?")
    val guess: String = StdIn.readLine().toLowerCase.trim
    val newCharacterSeq: Seq[GWCharacter] =
      if (randomCharacter.hairColour.toLowerCase == guess) {
        println(s"Yes! The mystery character has ${randomCharacter.hairColour} hair!")
        characterSeq.filter(character => character.hairColour == guess)
      } else {
        println(s"No... The mystery character doesn't have $guess hair.")
        characterSeq.filter(character => character.hairColour != guess)
      }

    println(s"You put down ${characterSeq.length - newCharacterSeq.length} people")
    newCharacterSeq
  }

  /**
   * Prompts the user to guess the character's hair length
   * @param randomCharacter Selected character to compare attribute
   * @param characterSeq Character sequence to filter
   * @return Reduced character sequence to continue the game
   */
  def hairLengthGuess(randomCharacter: GWCharacter, characterSeq: Seq[GWCharacter]): Seq[GWCharacter] = {
    // TODO: REFACTOR FOR TESTING
    println("Does the character have short or long hair?")
    val guess: String = StdIn.readLine().toLowerCase.trim
    val newCharacterSeq: Seq[GWCharacter] =
      if (randomCharacter.hairLength.toLowerCase == guess) {
        println(s"Yes! The mystery character has ${randomCharacter.hairLength} hair!")
        characterSeq.filter(character => character.hairLength == guess)
      } else {
        println(s"No... The mystery character doesn't have $guess hair.")
        characterSeq.filter(character => character.hairLength != guess)
      }

    println(s"You put down ${characterSeq.length - newCharacterSeq.length} people")
    newCharacterSeq
  }

  /**
   * Prompts the user to guess the character's accessory
   * @param randomCharacter Selected character to compare attribute
   * @param characterSeq Character sequence to filter
   * @return Reduced character sequence to continue the game
   */
  def accessoryGuess(randomCharacter: GWCharacter, characterSeq: Seq[GWCharacter]): Seq[GWCharacter] = {
    // TODO: REFACTOR FOR TESTING
    println("What accessory does the character have?")
    val guess: String = StdIn.readLine().toLowerCase.trim
    val newCharacterSeq: Seq[GWCharacter] =
      if (randomCharacter.accessory.toLowerCase == guess) {
        println(s"Yes! The mystery character does have ${randomCharacter.accessory}")
        characterSeq.filter(character => character.accessory == guess)
      } else {
        println(s"No... The mystery character doesn't have $guess")
        characterSeq.filter(character => character.accessory != guess)
      }

    println(s"You put down ${characterSeq.length - newCharacterSeq.length} people")
    newCharacterSeq
  }
}
