import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.currentlocation.MainActivity
import com.example.currentlocation.R
import com.example.currentlocation.SettingsFragment
import com.example.currentlocation.Home  // Import the Home class

class YourInitialFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_your_initial, container, false)

        // Handle the settings icon click
        val settingsIcon: ImageView = view.findViewById(R.id.settings_icon)
        settingsIcon.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, SettingsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // Handle the map icon click to open MainActivity
        val mapIcon: ImageView = view.findViewById(R.id.mapIcon)  // Assuming this is the map icon ID
        mapIcon.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        // Handle the home icon click to open Home fragment
        val imageView5: ImageView = view.findViewById(R.id.imageView5)  // Assuming this is the home icon ID
        imageView5.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, Home())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}
