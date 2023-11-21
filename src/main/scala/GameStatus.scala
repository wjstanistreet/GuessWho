/**
 * Game status
 * @param statusCode Status code of the game
 * @param board Board status for the game
 */
class GameStatus(statusCode: Int, board: Board) {
  // 0 - Continue processing
  // 1 - Game won
  // 2 - Print instructions
  // 3 - Error
  def getStatus: Int = statusCode

  def updateStatusNum(newStatus: Int): GameStatus = new GameStatus(newStatus, board)

  def getBoard: Board = board

  def updateStatusBoard(newBoard: Board): GameStatus = new GameStatus(statusCode, newBoard)
}
