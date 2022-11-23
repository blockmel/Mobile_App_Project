package de.hsas.inf.mobile_app_project.dataTypes

class Places {
    var id: Int = 0
    var location: String = "No given location"
    var name: String = "No name given"
    var gaelicName: String = "No gaelic name given"
    var placeTypeId: Int = 0
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun toString(): String {
        return "$id $location $name $gaelicName $placeTypeId $latitude $longitude"
    }
}