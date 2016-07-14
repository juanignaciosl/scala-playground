

class ConstantExamples {
  object A {
    val AA = "a"
  }

  class B {
    import A.AA

    print(AA)
  }

  class C {
    import A.{ AA => AAlias }

    print(AAlias)
  }

}