package de.hsas.inf.mobile_app_project.dataTypes

class PlaceTypes {
    var id: Int = 0
    var name: String = "No name given"
    var created_at: String = "No creation time"
    var updated_at: String = "Not updated"

    override fun toString(): String {
        return "$id $name $created_at $updated_at"
    }
}