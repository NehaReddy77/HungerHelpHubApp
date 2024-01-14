package mypackage.h2hub.activities
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mypackage.h2hub.R
import mypackage.h2hub.activities.viewmodel.InfoViewModel
import mypackage.h2hub.adapter.ApiDataAdapter

class InfoActivity : AppCompatActivity() {

    private val viewModel: InfoViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ApiDataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)
        setupToolbar()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ApiDataAdapter(emptyList()) // Initialize with an empty list
        recyclerView.adapter = adapter

        viewModel.apiData.observe(this, Observer { apiData ->
            adapter.setData(apiData)
        })

        // Assuming you want to fetch data for multiple food items
        val foodItems = listOf("egg", "banana", "apple", "orange", "tomato", "carrot", "broccoli", "chicken", "beef", "salmon")
        viewModel.fetchMultipleFoods(foodItems)
    }
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}
