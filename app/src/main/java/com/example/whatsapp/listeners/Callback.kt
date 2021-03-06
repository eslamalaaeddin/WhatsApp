package com.example.whatsapp.listeners

//to delegate the response to clicks to the hosting activity
interface Callback {
    //for group fragment clicks
    fun onGroupClicked(groupId:String)

    //for chat fragment clicks
    fun onUserChatClicked(userName:String, userId:String ,userImage:String)

    //for status  clicks
    fun onStatusClicked(id:String)

    //for call clicks
    fun onCallClicked(callerId:String)

}