package test.navigation.ui.fragment.develop
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.aachartmodel.aainfographics.aachartcreator.*
import com.github.aachartmodel.aainfographics.aaoptionsmodel.*
import com.github.aachartmodel.aainfographics.aatools.AAColor
import com.github.aachartmodel.aainfographics.aatools.AAGradientColor
import com.github.aachartmodel.aainfographics.aatools.AAJSStringPurer
import test.navigation.R
import test.navigation.databinding.FragmentDevelopBinding
import test.navigation.store.Account
import kotlin.math.sin

class DevelopFragment : Fragment(), AAChartView.AAChartViewCallBack {
    lateinit var binding: FragmentDevelopBinding
    lateinit var developViewModel: DevelopViewModel
    private var gradientColorsArr: Array<Any>? = null
    private var selectedGradientColor: Any = AAColor.Red


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupViewModel(inflater, container)
        binding.aaChartView.callBack = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChartView()
    }


    override fun onResume() {
        super.onResume()
        if(Account.RESULT.isNotEmpty()) {
            Log.e("loadResult Before", "Got numQuest ${Account.RESULT[0]["numQuest"]?.toInt()}")
            Log.e("loadResult Before", "Got numCorrect ${Account.RESULT[0]["numCorrect"]?.toInt()}")
            Log.e("loadResult Before", "Got countTest ${Account.RESULT.size}")
        }
    }


    private fun setupViewModel(inflater: LayoutInflater, container: ViewGroup?) {
        developViewModel = ViewModelProvider(this).get(DevelopViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_develop, container, false)
        binding.lifecycleOwner = this
    }

    private fun setupChartView() {
        binding.aaChartView.aa_drawChartWithChartOptions(configureChart())
    }

    private fun configureChart(): AAOptions {

        val gradientColorArr = arrayOf(
                AAGradientColor.OceanBlue,
                AAGradientColor.PurpleLake,
        )
        gradientColorsArr = gradientColorArr as Array<Any>

        return AAOptions()
                .chart(AAChart()
                        .type(AAChartType.Bar)
                        .scrollablePlotArea(AAScrollablePlotArea()
                                .minHeight(500)
                        ))
                .title(AATitle()
                        .text(""))
                .xAxis(AAXAxis()
                        .categories(developViewModel.arrayTimeStamp.value)
                        .gridLineWidth(2f))
                .yAxis(AAYAxis()
                        .allowDecimals(true)
                        .tickInterval(1)
                        .title(AATitle().text("num of sentences")))
                .series(arrayOf(configureSeriesDataArray()[0], configureSeriesDataArray()[1]))

    }

    private fun configureSeriesDataArray(): Array<AASeriesElement> {
        val gradientColorArr = arrayOf(
                AAGradientColor.OceanBlue,
                AAGradientColor.PurpleLake,
        )
        return arrayOf(
                AASeriesElement()
                        .name("numCorrect")
                        .data(developViewModel.arrayNumCorrect.value!! as Array<Any>)
                        .color(gradientColorArr[0]),
                AASeriesElement()
                        .name("numQuest")
                        .data(developViewModel.arrayNumQuest.value!! as Array<Any>)
                        .color(gradientColorArr[1])
        )
    }

    override fun chartViewDidFinishLoad(aaChartView: AAChartView) {

    }

    override fun chartViewMoveOverEventMessage(
        aaChartView: AAChartView,
        messageModel: AAMoveOverEventMessageModel
    ) {
        selectedGradientColor = gradientColorsArr?.get(messageModel.index!!)!!

        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            val aaSeriesElementsArr: Array<AASeriesElement> = arrayOf(
                AASeriesElement()
                    .data(configureSeriesDataArray() as Array<Any>)
            )
            binding.aaChartView.aa_onlyRefreshTheChartDataWithChartOptionsSeriesArray(
                aaSeriesElementsArr
            )
        }
    }
}