package com.allaboutscala

object HelloWorld {
    var am : String = _
    var ad : Int = _
    am = "Snow"
    ad = 5
    def main(args: Array[String]):Unit = {
        println("Hello, world!")
        println(am+ad)
    }

}

//By Extending the App we can avoid the boiler plate code

/*
object HelloWorld extends App{
     println("Hello, world!")
}
*/