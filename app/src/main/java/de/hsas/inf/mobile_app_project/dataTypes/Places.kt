package de.hsas.inf.mobile_app_project.dataTypes

class Places {
    var id: Int = 0
    var location: String = "No given location"
    var name: String = "No name given"
    var gaelic_name: String = "No gaelic name given"
    var place_type_id: Int = 0
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun toString(): String {
        return "$id $location $name $gaelic_name $place_type_id $latitude $longitude"
    }
}