package com.messfit.ai.domain.engine

import com.messfit.ai.data.model.Exercise
import com.messfit.ai.data.model.GymExperience
import com.messfit.ai.data.model.WorkoutDay
import com.messfit.ai.data.model.WorkoutSplit

object WorkoutEngine {

    fun generatePlan(experience: GymExperience, split: WorkoutSplit): List<WorkoutDay> {
        return when (split) {
            WorkoutSplit.PUSH_PULL_LEGS -> pushPullLegs(experience)
            WorkoutSplit.BRO_SPLIT -> broSplit(experience)
            WorkoutSplit.UPPER_LOWER -> upperLower(experience)
            WorkoutSplit.ARNOLD_SPLIT -> arnoldSplit(experience)
            WorkoutSplit.FULL_BODY -> fullBody(experience)
            WorkoutSplit.HOME_WORKOUT -> homeWorkout(experience)
        }
    }

    private fun exercise(
        name: String, sets: Int, reps: String, rest: Int,
        technique: String, mistakes: String, muscle: String
    ) = Exercise(name, sets, reps, rest, technique, mistakes, muscle)

    private fun pushPullLegs(exp: GymExperience): List<WorkoutDay> {
        val sets = if (exp == GymExperience.BEGINNER) 3 else 4
        return listOf(
            WorkoutDay("Day 1", "Push", listOf(
                exercise("Bench Press", sets, "8-12", 90, "Retract scapula, control descent", "Flaring elbows, bouncing bar", "Chest"),
                exercise("Overhead Press", sets, "8-10", 90, "Brace core, press vertically", "Excessive back lean", "Shoulders"),
                exercise("Incline Dumbbell Press", sets, "10-12", 75, "45° incline, full ROM", "Short range of motion", "Upper Chest"),
                exercise("Tricep Pushdown", 3, "12-15", 60, "Elbows pinned to sides", "Swinging body", "Triceps")
            )),
            WorkoutDay("Day 2", "Pull", listOf(
                exercise("Lat Pulldown", sets, "8-12", 90, "Pull to upper chest, squeeze lats", "Using momentum", "Lats"),
                exercise("Barbell Row", sets, "8-10", 90, "Hinge at hips, pull to belly", "Rounded back", "Back"),
                exercise("Face Pull", 3, "15-20", 60, "External rotation at end", "Too heavy weight", "Rear Delts"),
                exercise("Barbell Curl", 3, "10-12", 60, "No swinging", "Elbow drift forward", "Biceps")
            )),
            WorkoutDay("Day 3", "Legs", listOf(
                exercise("Squat", sets, "6-10", 120, "Depth below parallel, knees track toes", "Knee cave, half reps", "Quads/Glutes"),
                exercise("Romanian Deadlift", sets, "8-10", 90, "Hip hinge, slight knee bend", "Rounding lower back", "Hamstrings"),
                exercise("Leg Press", 3, "10-12", 90, "Full ROM without locking knees", "Locking knees", "Quads"),
                exercise("Calf Raise", 4, "15-20", 45, "Pause at top", "Bouncing reps", "Calves")
            ))
        )
    }

    private fun broSplit(exp: GymExperience): List<WorkoutDay> {
        val sets = if (exp == GymExperience.BEGINNER) 3 else 4
        return listOf(
            WorkoutDay("Monday", "Chest", listOf(
                exercise("Flat Bench Press", sets, "8-12", 90, "Arch slightly, drive through feet", "Bouncing bar off chest", "Chest")
            )),
            WorkoutDay("Tuesday", "Back", listOf(
                exercise("Pull-ups/Lat Pulldown", sets, "8-12", 90, "Full stretch at top", "Half reps", "Lats")
            )),
            WorkoutDay("Wednesday", "Shoulders", listOf(
                exercise("Overhead Press", sets, "8-10", 90, "Strict press, no leg drive", "Excessive arch", "Shoulders")
            )),
            WorkoutDay("Thursday", "Arms", listOf(
                exercise("Barbell Curl", 3, "10-12", 60, "Controlled tempo", "Swinging", "Biceps"),
                exercise("Skull Crushers", 3, "10-12", 60, "Elbows stable", "Flaring elbows", "Triceps")
            )),
            WorkoutDay("Friday", "Legs", listOf(
                exercise("Squat", sets, "6-10", 120, "Brace core before descent", "Good morning squat", "Legs")
            ))
        )
    }

    private fun upperLower(exp: GymExperience): List<WorkoutDay> {
        val sets = if (exp == GymExperience.BEGINNER) 3 else 4
        return listOf(
            WorkoutDay("Day 1", "Upper A", listOf(
                exercise("Bench Press", sets, "8-12", 90, "Controlled eccentric", "Flared elbows", "Chest"),
                exercise("Barbell Row", sets, "8-10", 90, "Pull to hip", "Shrugging", "Back")
            )),
            WorkoutDay("Day 2", "Lower A", listOf(
                exercise("Squat", sets, "6-10", 120, "Break parallel", "Knee valgus", "Quads"),
                exercise("RDL", sets, "8-10", 90, "Feel hamstring stretch", "Rounding back", "Hamstrings")
            )),
            WorkoutDay("Day 3", "Upper B", listOf(
                exercise("Overhead Press", sets, "8-10", 90, "Glutes squeezed", "Back hyperextension", "Shoulders"),
                exercise("Pull-ups", sets, "6-10", 90, "Dead hang start", "Kipping", "Lats")
            )),
            WorkoutDay("Day 4", "Lower B", listOf(
                exercise("Leg Press", sets, "10-12", 90, "Feet shoulder width", "Locking knees", "Quads"),
                exercise("Leg Curl", 3, "12-15", 60, "Slow negative", "Hip rise", "Hamstrings")
            ))
        )
    }

    private fun arnoldSplit(exp: GymExperience): List<WorkoutDay> {
        val sets = if (exp == GymExperience.BEGINNER) 3 else 4
        return listOf(
            WorkoutDay("Day 1", "Chest & Back", listOf(
                exercise("Bench Press", sets, "8-12", 90, "Superset with rows for advanced", "Poor form under fatigue", "Chest"),
                exercise("Barbell Row", sets, "8-10", 90, "Chest supported if needed", "Jerking weight", "Back")
            )),
            WorkoutDay("Day 2", "Shoulders & Arms", listOf(
                exercise("Overhead Press", sets, "8-10", 90, "Full lockout", "Partial reps", "Shoulders"),
                exercise("Lateral Raise", 3, "12-15", 60, "Lead with elbows", "Shrugging traps", "Side Delts")
            )),
            WorkoutDay("Day 3", "Legs", listOf(
                exercise("Squat", sets, "6-10", 120, "Consistent depth", "Cutting depth", "Legs"),
                exercise("Lunges", 3, "10/leg", 75, "Knee over ankle", "Leaning forward", "Glutes")
            ))
        )
    }

    private fun fullBody(exp: GymExperience): List<WorkoutDay> {
        val sets = 3
        return listOf(
            WorkoutDay("Day 1", "Full Body A", listOf(
                exercise("Squat", sets, "8-10", 90, "Brace before each rep", "Knee cave", "Legs"),
                exercise("Bench Press", sets, "8-10", 90, "Feet planted", "Bouncing", "Chest"),
                exercise("Barbell Row", sets, "8-10", 90, "Neutral spine", "Rounded back", "Back")
            )),
            WorkoutDay("Day 2", "Rest", emptyList()),
            WorkoutDay("Day 3", "Full Body B", listOf(
                exercise("Deadlift", sets, "5-8", 120, "Hip hinge pattern", "Rounding spine", "Posterior Chain"),
                exercise("Overhead Press", sets, "8-10", 90, "Squeeze glutes", "Excessive lean", "Shoulders"),
                exercise("Lat Pulldown", sets, "10-12", 75, "Full stretch", "Behind neck", "Lats")
            ))
        )
    }

    private fun homeWorkout(exp: GymExperience): List<WorkoutDay> {
        return listOf(
            WorkoutDay("Day 1", "Home Upper", listOf(
                exercise("Push-ups", 4, "12-20", 60, "Full ROM, chest to floor", "Sagging hips", "Chest"),
                exercise("Inverted Rows", 3, "10-15", 75, "Body straight line", "Partial reps", "Back"),
                exercise("Pike Push-ups", 3, "10-12", 60, "Hips high", "Bent legs", "Shoulders")
            )),
            WorkoutDay("Day 2", "Home Lower", listOf(
                exercise("Bodyweight Squats", 4, "15-25", 60, "Depth below parallel", "Knee valgus", "Quads"),
                exercise("Bulgarian Split Squat", 3, "10/leg", 75, "Front knee stable", "Leaning", "Glutes"),
                exercise("Glute Bridge", 3, "15-20", 45, "Squeeze at top", "Hyperextending", "Glutes")
            )),
            WorkoutDay("Day 3", "Home Core & Cardio", listOf(
                exercise("Plank", 3, "45-60s", 45, "Neutral spine", "Hip sag", "Core"),
                exercise("Mountain Climbers", 3, "30s", 45, "Fast controlled", "Bouncing hips", "Core/Cardio")
            ))
        )
    }
}
