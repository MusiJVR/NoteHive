package com.notehive.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.notehive.R

class AboutApplicationActivity : AppCompatActivity() {
    private val GITHUB_URL = "https://github.com/MusiJVR/NoteHive"
    private val MIT_LICENSE_URL = "https://github.com/MusiJVR/NoteHive/blob/main/LICENSE.md"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_application)

        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        findViewById<TextView>(R.id.appVersion).text = getString(R.string.version, versionName)

        findViewById<TextView>(R.id.githubLink).setOnClickListener {
            openLink(GITHUB_URL)
        }

        findViewById<TextView>(R.id.licenseLink).setOnClickListener {
            openLink(MIT_LICENSE_URL)
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
