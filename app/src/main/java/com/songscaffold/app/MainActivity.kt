package com.songscaffold.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.songscaffold.app.data.PromptRepository
import com.songscaffold.app.model.SongStep
import com.songscaffold.app.ui.HomeScreen
import com.songscaffold.app.ui.SettingsScreen
import com.songscaffold.app.ui.StepScreen
import com.songscaffold.app.ui.SummaryScreen
import com.songscaffold.app.ui.theme.SongScaffoldTheme
import com.songscaffold.app.viewmodel.SettingsViewModel
import com.songscaffold.app.viewmodel.SongIdeaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SongScaffoldTheme {
                SongScaffoldApp()
            }
        }
    }
}

@Composable
fun SongScaffoldApp() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = viewModel()
    val songIdeaViewModel: SongIdeaViewModel = viewModel()

    val settings by settingsViewModel.settings.collectAsState()
    val songIdea by songIdeaViewModel.songIdea.collectAsState()
    val enabledSteps by songIdeaViewModel.enabledSteps.collectAsState()

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onStartSongIdea = {
                    songIdeaViewModel.startSession(settings)
                    navController.navigate("step/0")
                },
                onSettings = { navController.navigate("settings") }
            )
        }

        composable("step/{stepIndex}") { backStackEntry ->
            val stepIndex = backStackEntry.arguments?.getString("stepIndex")?.toIntOrNull() ?: 0

            if (enabledSteps.isEmpty() || stepIndex >= enabledSteps.size) {
                navController.navigate("summary") { popUpTo("home") }
                return@composable
            }

            val step = enabledSteps[stepIndex]

            StepScreen(
                step = step,
                stepIndex = stepIndex,
                totalSteps = enabledSteps.size,
                topic = songIdea.topic,
                rhymeWord = songIdea.rhymeWord,
                selectedOption = selectedOptionFor(step, songIdea),
                selectedProgression = songIdea.chordProgression,
                onTopicRandom = {
                    songIdeaViewModel.setTopic(songIdeaViewModel.randomTopic())
                },
                onRhymeWordRandom = {
                    songIdeaViewModel.setRhymeWord(songIdeaViewModel.randomRhymeWord())
                },
                onOptionSelected = { option ->
                    applyOption(step, option, songIdeaViewModel)
                },
                onProgressionSelected = { progression ->
                    songIdeaViewModel.setChordProgression(progression)
                },
                onRandom = {
                    applyRandom(step, songIdeaViewModel)
                },
                onSkip = {
                    navigateStep(navController, stepIndex, enabledSteps, forward = true)
                },
                onNext = {
                    navigateStep(navController, stepIndex, enabledSteps, forward = true)
                },
                onBack = {
                    if (stepIndex == 0) {
                        navController.popBackStack("home", inclusive = false)
                    } else {
                        navigateStep(navController, stepIndex, enabledSteps, forward = false)
                    }
                }
            )
        }

        composable("summary") {
            SummaryScreen(
                songIdea = songIdea,
                onStartOver = {
                    songIdeaViewModel.startSession(settings)
                    navController.navigate("step/0") {
                        popUpTo("home") { inclusive = false }
                    }
                },
                onHome = {
                    songIdeaViewModel.reset()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                settings = settings,
                onToggle = { update -> settingsViewModel.toggle(update) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private fun navigateStep(
    navController: NavHostController,
    currentIndex: Int,
    steps: List<SongStep>,
    forward: Boolean
) {
    val nextIndex = if (forward) currentIndex + 1 else currentIndex - 1
    if (forward && nextIndex >= steps.size) {
        navController.navigate("summary") {
            popUpTo("home") { inclusive = false }
        }
    } else if (forward && nextIndex < steps.size) {
        navController.navigate("step/$nextIndex") {
            popUpTo("step/$currentIndex") { inclusive = true }
        }
    } else if (!forward && nextIndex >= 0) {
        navController.navigate("step/$nextIndex") {
            popUpTo("step/$currentIndex") { inclusive = true }
        }
    }
}

private fun selectedOptionFor(step: SongStep, songIdea: com.songscaffold.app.model.SongIdea): String? =
    when (step) {
        SongStep.POINT_OF_VIEW -> songIdea.pointOfView
        SongStep.DELIVERY_MODE -> songIdea.deliveryMode
        SongStep.PHRASING_STYLE -> songIdea.phrasingStyle
        SongStep.EMOTIONAL_INTENSITY -> songIdea.emotionalIntensity
        SongStep.SONG_KEY -> songIdea.songKey
        SongStep.STARTING_NOTE -> songIdea.startingNote
        SongStep.SECOND_NOTE_DIRECTION -> songIdea.secondNoteDirection
        SongStep.RHYME_SCHEME -> songIdea.rhymeScheme
        else -> null
    }

private fun applyOption(step: SongStep, option: String, vm: SongIdeaViewModel) {
    when (step) {
        SongStep.POINT_OF_VIEW -> vm.setPointOfView(option)
        SongStep.DELIVERY_MODE -> vm.setDeliveryMode(option)
        SongStep.PHRASING_STYLE -> vm.setPhrasingStyle(option)
        SongStep.EMOTIONAL_INTENSITY -> vm.setEmotionalIntensity(option)
        SongStep.SONG_KEY -> vm.setSongKey(option)
        SongStep.STARTING_NOTE -> vm.setStartingNote(option)
        SongStep.SECOND_NOTE_DIRECTION -> vm.setSecondNoteDirection(option)
        SongStep.RHYME_SCHEME -> vm.setRhymeScheme(option)
        else -> Unit
    }
}

private fun applyRandom(step: SongStep, vm: SongIdeaViewModel) {
    when (step) {
        SongStep.RHYME_WORD -> vm.setRhymeWord(vm.randomRhymeWord())
        SongStep.POINT_OF_VIEW -> vm.setPointOfView(PromptRepository.pointOfViewOptions.random())
        SongStep.DELIVERY_MODE -> vm.setDeliveryMode(PromptRepository.deliveryModes.random())
        SongStep.PHRASING_STYLE -> vm.setPhrasingStyle(PromptRepository.phrasingStyles.random())
        SongStep.EMOTIONAL_INTENSITY -> vm.setEmotionalIntensity(PromptRepository.emotionalIntensityOptions.random())
        SongStep.CHORD_PROGRESSION -> vm.setChordProgression(PromptRepository.chordProgressions.random())
        SongStep.SONG_KEY -> vm.setSongKey(PromptRepository.majorKeys.random())
        SongStep.STARTING_NOTE -> vm.setStartingNote(PromptRepository.startingNoteOptions.random())
        SongStep.SECOND_NOTE_DIRECTION -> vm.setSecondNoteDirection(PromptRepository.secondNoteDirectionOptions.random())
        SongStep.RHYME_SCHEME -> vm.setRhymeScheme(PromptRepository.rhymeSchemes.random())
        else -> Unit
    }
}
