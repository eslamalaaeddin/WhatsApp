package com.example.whatsapp.helpers

import com.example.whatsapp.R
import com.example.whatsapp.adapters.ContactAdapterFromFirebase
import com.example.whatsapp.adapters.GroupsAdapter
import com.example.whatsapp.models.ContactsModel
import com.example.whatsapp.ui.fragments.ChatsFragment

/*
    This util class is for string constants,
    and repeated functions and declarations
 */
object Utils {
    const val CONTACTS_CHILD = "Contacts"
    const val GROUPS_CHILD = "Groups"
    const val USERS_CHILD = "Users"
    const val CHAT_REQUESTS_CHILD = "Chat requests"
    const val MESSAGES_CHILD = "Messages"

    const val NAME_CHILD = "name"
    const val IMAGE_CHILD = "image"
    const val STATUS_CHILD = "status"
    const val USER_ID_CHILD = "uid"
    const val STATE_CHILD = "state"

    const val DEVICE_TOKEN_CHILD = "device token"



    const val BASE_URL = "https://fcm.googleapis.com/"
    const val SERVER_KEY = "AAAAgHSEVb8:APA91bGoMUegjCz40u7Qp4YFt4nzatjtDKi3DJxLeH0ZBd6YmonNyyFQyiLim-iryZAoAnpB9Y9vI2R15KOLvL_zjyfs4UIcu-R678v0jEx1A8NWXor8C0W357nif7ohQnWcIuxrhUXZ"
    const val CONTENT_TYPE = "application/json"

    var senderId = ""

    var privateChatsAdapter : ContactAdapterFromFirebase? = null
    var groupsChatAdapter : GroupsAdapter? = null

    var dummyList = mutableListOf<ContactsModel>()



    val COLORS = arrayOf(
        R.color.dark_orange,
        R.color.purple,
        R.color.light_yellow,
        R.color.light_blue,
        R.color.light_green,
        R.color.dark_green,
        R.color.light_orange,
        R.color.dark_yellow
    )

    const val PRIMARY_COLOR = R.color.colorPrimary

    var gid = ""

}