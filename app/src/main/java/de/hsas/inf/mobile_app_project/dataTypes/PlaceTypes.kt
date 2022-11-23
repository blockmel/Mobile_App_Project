package de.hsas.inf.mobile_app_project.dataTypes

class PlaceTypes {
    var id: Int = 0
    var name: String = "No name given"
    var createdAt: String = "No creation time"
    var updatedAt: String = "Not updated"

    override fun toString(): String {
        return "$id $name $createdAt $updatedAt"
    }
}