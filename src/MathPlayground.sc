object MathPlayground {

  def squareNTimes(count: Int) = {
    List.tabulate(count)(n => List.fill(count)(n * n))
  }                                               //> squareNTimes: (count: Int)List[List[Int]]

  squareNTimes(5)                                 //> res0: List[List[Int]] = List(List(0, 0, 0, 0, 0), List(1, 1, 1, 1, 1), List(
                                                  //| 4, 4, 4, 4, 4), List(9, 9, 9, 9, 9), List(16, 16, 16, 16, 16))
}