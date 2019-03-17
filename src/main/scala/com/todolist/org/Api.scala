package com.todolist.org

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.{NotFound, OK}
import akka.http.scaladsl.server.Directives.{as, complete, delete, entity, get, onSuccess, path, pathPrefix, post, put}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import com.todolist.org.Models.Todo

object Api extends App with JsonSupport with CORSHandler {

  implicit val system = ActorSystem("todolist")
  implicit val materializer = ActorMaterializer()
  implicit val ex = system.dispatcher

  import Services._

  def routes =
    pathPrefix("todos") {
      get {
        onSuccess(getTodoList()) { list =>
          complete(list)
        }
      } ~
        post {
          entity(as[Todo]) { todo =>
            onSuccess(postTodo(todo)) { r =>
              complete(todo.copy(id = r))
            }
          }
        } ~
        get {
          path(IntNumber) { id =>
            onSuccess(getTodo(id)) { todo =>
              todo match {
                case Some(x) => complete(x)
                case None => complete(NotFound)
              }
            }
          }
        } ~
        put {
          path(IntNumber) { id =>
            entity(as[Todo]) { todo =>
              val newTodo = todo.copy(id = id)
              onSuccess(putTodo(newTodo)) { r =>
                complete(newTodo)
              }
            }
          }
        } ~
        delete {
          path(IntNumber) { id =>
            onSuccess(deleteTodo(id)) { r =>
              complete(OK)
            }
          }
        }
    }


  Http().bindAndHandle(corsHandler(routes), "0.0.0.0", 8080)
}
