import org.scalatest.FlatSpec

class GWCharacterSpec extends FlatSpec{

  "A Person" should "have a name" in {
    val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
    assert(me.name === "William Stanistreet")
  }

  "A Person" should "have a gender" in {
      val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
      assert(me.gender === "Male")
    }

  "A Person" should "have an age" in {
      val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
      assert(me.age === 24)
    }

  "A Person" should "have a hair colour" in {
    val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
    assert(me.hairColour === "brown")
  }

  "A Person" should "have a hair length" in {
    val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
    assert(me.hairLength === "short")
  }

  "A Person" should "have an accessory" in {
    val me: GWCharacter = GWCharacter("William Stanistreet", "Male", 24, "short", "brown", "a green hat")
    assert(me.accessory === "a green hat")
  }
}
