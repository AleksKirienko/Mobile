package ru.sibsutis.database

class User {
    var id: Int = 0
    var name: String = ""
    var age: Int = 0
    var weight: Int = 0
    var height: Int = 0

    constructor() {}

    constructor(name: String, age: Int, weight: Int, height: Int) {
        this.name = name
        this.age = age
        this.weight = weight
        this.height = height
    }

}