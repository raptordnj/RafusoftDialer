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
package org.linphone.activities.main.about

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList
import org.linphone.R
import org.linphone.activities.main.adapters.AboutLinkListAdapter
import org.linphone.activities.main.fragments.SecureFragment
import org.linphone.classes.LinkItem
import org.linphone.core.tools.Log
import org.linphone.databinding.AboutFragmentBinding

class AboutFragment : SecureFragment<AboutFragmentBinding>() {
    private lateinit var viewModel: AboutViewModel

    override fun getLayoutId(): Int = R.layout.about_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel = ViewModelProvider(this)[AboutViewModel::class.java]
        binding.viewModel = viewModel

        binding.setPrivacyPolicyClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.about_privacy_policy_link))
            )
            try {
                startActivity(browserIntent)
            } catch (se: SecurityException) {
                Log.e("[About] Failed to start browser intent, $se")
            }
        }

        binding.setLicenseClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.about_license_link))
            )
            try {
                startActivity(browserIntent)
            } catch (se: SecurityException) {
                Log.e("[About] Failed to start browser intent, $se")
            }
        }

        binding.setWeblateClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.about_weblate_link))
            )
            try {
                startActivity(browserIntent)
            } catch (se: SecurityException) {
                Log.e("[About] Failed to start browser intent, $se")
            }
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.aboutLinks)
        val data = LinkedList<LinkItem>() // Replace 'String' with your data type
        data.add(LinkItem("https://rafusoft.com", "Check Update"))
        data.add(LinkItem("https://rafusoft.com/privacy-policy", "Privacy Policy"))
        data.add(LinkItem("https://rafusoft.com", "Share Application"))
        data.add(LinkItem("https://rafusoft.com/contact", "Support"))

        // Populate 'data' with your LinkedList items

        // Populate 'data' with your LinkedList items
        val adapter = AboutLinkListAdapter(requireContext(), data)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}
