package com.todolist.org

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.todolist.org.Models.Todo
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val td = jsonFormat4(Todo.apply)
}
