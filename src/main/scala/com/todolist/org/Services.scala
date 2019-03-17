package com.todolist.org

import com.todolist.org.Models._
import slick.jdbc.H2Profile.api._
import slick.lifted.Tag

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Services {

  lazy val db = Database.forConfig("todolist")
  lazy val todoTable = TableQuery[TodoTable]
  Await.result(db.run(todoTable.schema.create),2.seconds)

  class TodoTable(tag: Tag) extends Table[Todo](tag, "todolist") {
    val id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    val todo = column[String]("todo")
    val adAt = column[String]("startAt")
    val duAt = column[String]("endAt")

    override def * = (id,todo, adAt, duAt).mapTo[Todo]
  }

  def getTodoList():Future[Seq[Todo]]={
    db.run(todoTable.result)
  }

  def getTodo(id: Int): Future[Option[Todo]] = {
    db.run(todoTable.filter(e => e.id === id).result.headOption)
  }

  def postTodo(todo: Todo): Future[Int] = {
    db.run(todoTable returning todoTable.map(_.id) += todo )
  }

  def putTodo(todo: Todo): Future[Int] = {
    db.run(todoTable.filter(e => e.id === todo.id).update(todo))
  }

  def deleteTodo(id: Int): Future[Int] = {
    db.run(todoTable.filter(e => e.id === id).delete)
  }

}
