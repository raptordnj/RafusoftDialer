/*
 * Copyright (c) 2010-2020 Belledonne Communications SARL.
 *
 * This file is part of linphone-android
 * (see https://www.linphone.org).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.linphone.activities.main.chat.data

import androidx.lifecycle.MutableLiveData
import org.linphone.LinphoneApplication.Companion.coreContext
import org.linphone.R
import org.linphone.contact.GenericContactData
import org.linphone.core.ChatRoom
import org.linphone.core.Participant
import org.linphone.utils.LinphoneUtils

class DevicesListGroupData(private val participant: Participant) : GenericContactData(
    participant.address
) {
    val securityLevelIcon: Int by lazy {
        when (participant.securityLevel) {
            ChatRoom.SecurityLevel.Safe -> R.drawable.security_2_indicator
            ChatRoom.SecurityLevel.Encrypted -> R.drawable.security_1_indicator
            else -> R.drawable.security_alert_indicator
        }
    }

    val securityLevelContentDescription: Int by lazy {
        when (participant.securityLevel) {
            ChatRoom.SecurityLevel.Safe -> R.string.content_description_security_level_safe
            ChatRoom.SecurityLevel.Encrypted -> R.string.content_description_security_level_encrypted
            else -> R.string.content_description_security_level_unsafe
        }
    }

    val sipUri: String get() = LinphoneUtils.getDisplayableAddress(participant.address)

    val isExpanded = MutableLiveData<Boolean>()

    val devices = MutableLiveData<ArrayList<DevicesListChildData>>()

    init {
        securityLevel.value = participant.securityLevel
        isExpanded.value = false

        val list = arrayListOf<DevicesListChildData>()
        for (device in participant.devices) {
            list.add(DevicesListChildData((device)))
        }
        devices.value = list
    }

    fun toggleExpanded() {
        isExpanded.value = isExpanded.value != true
    }

    fun onClick() {
        val device = if (participant.devices.isEmpty()) null else participant.devices.first()
        if (device?.address != null) {
            coreContext.startCall(device.address, forceZRTP = true)
        } else {
            coreContext.startCall(participant.address, forceZRTP = true)
        }
    }
}
