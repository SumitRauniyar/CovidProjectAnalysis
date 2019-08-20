package com.allaboutscala

object CommandLine extends App{
  var m=5
  print("The Command Line Arguments are :")
  m=m+1
  print(args.mkString(","))
}
