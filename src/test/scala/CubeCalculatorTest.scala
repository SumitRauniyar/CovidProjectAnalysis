import com.allaboutscala.CubeCalculator
import org.scalatest.FunSuite

class CubeCalculatorTest extends FunSuite {
  test("CubeCalculator.cube") {
    assert(CubeCalculator.cube(3) === 23)
    assert(CubeCalculator.cube(0) === 1)
  }
}