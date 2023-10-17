package com.example.plugins

import java.sql.*


fun logInDatabase(user: User): Boolean {
    val url = "jdbc:mysql://localhost/user_database"
    val usr = "app_admin"
    val pswd = "1243maksim"

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, usr, pswd)

        val selectQuery = "SELECT * FROM users WHERE username = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, user.username)

        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            val storedPassword = resultSet.getString("password")

            if (storedPassword == user.password) {
                return true
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }

    return false
}

fun checkIfUserExists(username: Username): Boolean {
    val url = "jdbc:mysql://localhost/user_database"
    val usr = "app_admin"
    val pswd = "1243maksim"

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, usr, pswd)

        val selectQuery = "SELECT * FROM users WHERE username = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, username.username)

        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
                return true
        }

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }

    return false
}

fun setImage(setImage: SetImage) {
    val url = "jdbc:mysql://localhost/user_database"
    val user = "app_admin"
    val pswd = "1243maksim"

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, user, pswd)

        val selectQuery = "SELECT COUNT(*) FROM profile_image WHERE username = ?"
        val selectStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        selectStatement.setString(1, setImage.username)
        val resultSet: ResultSet = selectStatement.executeQuery()

        if (resultSet.next() && resultSet.getInt(1) > 0) {
            val updateQuery = "UPDATE profile_image SET image = ? WHERE username = ?"
            val updateStatement: PreparedStatement = connection.prepareStatement(updateQuery)
            updateStatement.setBytes(1, setImage.image)
            updateStatement.setString(2, setImage.username)
            val rowsAffected = updateStatement.executeUpdate()

            if (rowsAffected > 0) {
                println("Image updated successfully")
            } else {
                println("Failed to update image")
            }
        } else {
            val insertQuery = "INSERT INTO profile_image (username, image) VALUES (?, ?)"
            val insertStatement: PreparedStatement = connection.prepareStatement(insertQuery)
            insertStatement.setString(1, setImage.username)
            insertStatement.setBytes(2, setImage.image)
            val rowsAffected = insertStatement.executeUpdate()

            if (rowsAffected > 0) {
                println("Image inserted successfully")
            } else {
                println("Failed to insert image")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }
}


fun insertDatabase(student: Student) {
    val url = "jdbc:mysql://localhost/user_database"
    val usr = "app_admin"
    val pswd = "1243maksim"

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, usr, pswd)

        val insertQuery = "insert into users (username, password, first_name, last_name, study_group) values (?, ?, ?, ?, ?)"

        val preparedStatement: PreparedStatement = connection.prepareStatement(insertQuery)
        preparedStatement.setString(1, student.username)
        preparedStatement.setString(2, student.password)
        preparedStatement.setString(3, student.first_name)
        preparedStatement.setString(4, student.last_name)
        preparedStatement.setString(5, student.study_group)

        val affectedLines = preparedStatement.executeUpdate()

        if (affectedLines > 0) {
            println("User added")
        } else {
            println("Couldn't add user")
        }

    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }
}

fun selectLastName(username: Username): String {
    val url = "jdbc:mysql://localhost/user_database"
    val user = "app_admin"
    val password = "1243maksim"
    var storedLastName =  ""
    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, user, password)

        val selectQuery = "select * from users where username = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, username.username)

        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            storedLastName = resultSet.getString("last_name")
        }
    } catch(e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }
    return storedLastName
}

fun selectFirstName(username: Username): String {
    val url = "jdbc:mysql://localhost/user_database"
    val user = "app_admin"
    val password = "1243maksim"
    var storedFirstName =  ""
    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, user, password)

        val selectQuery = "select * from users where username = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, username.username)

        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            storedFirstName = resultSet.getString("first_name")
        }
    } catch(e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }
    return storedFirstName
}

fun selectGroup(username: Username): String {
    val url = "jdbc:mysql://localhost/user_database"
    val user = "app_admin"
    val password = "1243maksim"
    var storedGroup =  ""
    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, user, password)

        val selectQuery = "select * from users where username = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, username.username)

        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            storedGroup = resultSet.getString("study_group")
        }
    } catch(e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }
    return storedGroup
}

fun getImage(getImage: GetImage): ByteArray? {
    val url = "jdbc:mysql://localhost/user_database"
    val user = "app_admin"
    val pswd = "1243maksim"
    var result: ByteArray? = null
    var connection: Connection? = null
    var preparedStatement: PreparedStatement? = null
    var resultSet: ResultSet? = null

    try {
        connection = DriverManager.getConnection(url, user, pswd)
        val selectQuery = "select * from profile_image where username = ?"
        preparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, getImage.username)

        resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            val storedImage = resultSet.getBytes("image")
            if (storedImage != null) {
                result = storedImage
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        resultSet?.close()
        preparedStatement?.close()
        connection?.close()
    }

    return result
}


fun selectPassword(user: Username):String {
    val url = "jdbc:mysql://localhost/user_database"
    val usr = "app_admin"
    val pswd = "1243maksim"
    var storedPassword = ""
    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, usr, pswd)

        val selectQuery = "select * from users where username = ?"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)
        preparedStatement.setString(1, user.username)

        val resultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            storedPassword = resultSet.getString("password")
        }
    } catch(e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }
    return storedPassword
}

fun getAllUsersFromDatabase(): List<Student> {
    val url = "jdbc:mysql://localhost/user_database"
    val usr = "app_admin"
    val pswd = "1243maksim"

    val users = mutableListOf<Student>()

    var connection: Connection? = null
    try {
        connection = DriverManager.getConnection(url, usr, pswd)

        val selectQuery = "SELECT * FROM users"
        val preparedStatement: PreparedStatement = connection.prepareStatement(selectQuery)

        val resultSet = preparedStatement.executeQuery()
        while (resultSet.next()) {
            val username = resultSet.getString("username")
            val password = resultSet.getString("password")
            val firstName = resultSet.getString("first_name")
            val lastName = resultSet.getString("last_name")
            val studyGroup = resultSet.getString("study_group")

            val user = Student(username, password, firstName, lastName, studyGroup)
            users.add(user)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        connection?.close()
    }

    return users
}