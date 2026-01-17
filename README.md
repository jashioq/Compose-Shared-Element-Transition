# Shared Element Transitions with Synchronized Animations

A plant catalog app demonstrating advanced Jetpack Compose shared element transitions combined with custom animations. Shows how to synchronize SharedTransitionScope bounds animations with non-standard property animations like corner radius morphing.

## How It Works

This app coordinates multiple animations across navigation transitions using a 3-stage entry sequence and synchronized corner radius animation.

---

### **Stage 1: Shared Element Bounds Transition (0-1000ms)**

SharedTransitionScope animates four shared elements from list to detail:

```kotlin
// In PlantCard (list screen)
Box(
    modifier = Modifier
        .sharedElement(
            sharedContentState = rememberSharedContentState(key = "box-${plant.id}"),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = boundsTransform // 1000ms tween
        )
)
```

**Shared elements:**
- Box container (card to full content box)
- Plant image (thumbnail to full-width)
- Plant name text
- Scientific name text

All bounds animate simultaneously using the same 1000ms duration.

---

### **Stage 2: Corner Radius Animation (0-1000ms)**

A custom corner radius animation runs alongside the bounds transition:

```kotlin
// In Navigation.kt
val currentRadius by animateFloatAsState(
    targetValue = cornerRadiusState.cornerRadius,
    animationSpec = tween(durationMillis = 1000) // Same duration as bounds
)
```

**Why this works:**
- Same 1000ms duration keeps corner radius in sync with bounds
- Both list card and detail screen receive the animated radius value
- List card applies radius conditionally based on selected plant ID
- Detail screen applies radius to both image and content box

**Conditional radius application in list:**

```kotlin
cornerRadius = if (plant.id == selectedPlantId) cornerRadius else CornerRadiusLarge
```

This ensures only the selected card animates while others stay static.

---

### **Stage 3: Content Expansion and Fade (1000-1500ms)**

After bounds finish, content animations begin:

```kotlin
// Entry animation sequence
delay(1000.milliseconds) // Wait for bounds

// Enable wrap content (triggers height change)
_animationState.update { it.copy(boxShouldWrapContent = true) }

// Fade in content
_animationState.update { it.copy(contentVisibilityTarget = 1f) }
delay(500.milliseconds)

// Enable drag interaction
_animationState.update { it.copy(dragEnabled = true) }
```

**What happens:**
- Box switches from fillMaxSize to wrapContentHeight
- Height change triggers automatic offset adjustment
- Content alpha animates from 0f to 1f (500ms)
- Background color lerps from white to gray
- Drag handle, difficulty badge, and care instructions fade in
- Shadow opacity animates with content visibility

---

### **Height-Dependent Offset Adjustment**

The content box automatically centers itself when height changes:

```kotlin
// Track heights before and after expansion
var boxHeightBeforeDrag by remember { mutableFloatStateOf(0f) }
var boxHeightAfterDrag by remember { mutableFloatStateOf(0f) }

fun getOffsetAdjustment(): Float {
    return if (boxHeightAfterDrag > 0f && boxHeightBeforeDrag > 0f) {
        floor(((boxHeightAfterDrag - boxHeightBeforeDrag) / 2).coerceAtLeast(0f))
    } else {
        0f
    }
}

// Apply offset with adjustment
.offset {
    val adjustment = getOffsetAdjustment()
    IntOffset(0, (boxOffset + adjustment).roundToInt())
}
```

**Why this is needed:**
- Box height increases when content becomes visible
- Without adjustment, box would jump upward
- Adjustment = (expandedHeight - collapsedHeight) / 2
- Centers the box during height transition

---

### **Drag Interaction with Dynamic Bounds**

After animations complete, the content box becomes draggable:

```kotlin
val draggableState = remember {
    DraggableState { delta ->
        // Calculate drag limits from content height
        val maxDragDistance = -(boxHeightAfterDrag - boxHeightBeforeDrag)
        viewModel.updateDragOffset(
            delta, 
            minOffset = maxDragDistance, // Can drag up to reveal content
            maxOffset = 0f // Cannot drag down
        )
    }
}
```

**Drag bounds calculation:**
- minOffset = -(expanded height - collapsed height)
- maxOffset = 0f (natural position)
- User can drag up to fully reveal content
- Cannot drag below natural position

**Conditional easing:**

```kotlin
animationSpec = tween(
    durationMillis = animationState.boxOffsetAnimationSpeed.toInt(),
    easing = if (animationState.boxOffsetAnimationSpeed == 30f)
        LinearEasing // During drag
    else
        FastOutSlowInEasing // During exit
)
```

---

### **Exit Animation Sequence**

Reverses the entry stages:

```kotlin
// Stage 1: Fade out content (0-500ms)
_animationState.update {
    it.copy(
        dragEnabled = false,
        contentVisibilityTarget = 0f,
        boxOffsetYTarget = 0f
    )
}
delay(500.milliseconds)

// Stage 2: Reset layout simultaneously
_animationState.update {
    it.copy(
        boxShouldWrapContent = false,
        offsetAdjustment = 0f // Critical: same emission
    )
}

// Navigation callback
onComplete()
```

**Critical detail:**
- boxShouldWrapContent and offsetAdjustment reset in same state emission
- Prevents visual flickering during bounds transition back to list
- Corner radius animates back to 32dp in Navigation

---

## Unified Content Visibility

A single state variable drives all visibility animations:

```kotlin
data class AnimationState(
    val contentVisibilityTarget: Float = 0f // 0f to 1f
)
```

**Controls:**
- Background color lerp (white to gray)
- Back button fade-in
- Content alpha (text, badge, instructions)
- Shadow opacity
- Drag handle visibility

**Implementation:**

```kotlin
// Background color
val backgroundColor by animateColorAsState(
    targetValue = lerp(
        InitialDetailBackground, // White
        DetailBackground, // Gray
        animationState.contentVisibilityTarget
    )
)

// Content elements
Text(
    text = plant.details,
    modifier = Modifier.alpha(contentVisibility)
)
```

---

## Key Techniques Summary

**1. Synchronized durations:**
- Bounds transition: 1000ms
- Corner radius: 1000ms
- Content fade: 500ms

**2. State-driven animations:**
- Single source of truth (ViewModel state)
- StateFlow triggers reactive UI updates
- Coroutine delays sequence stages

**3. Conditional modifiers:**
- List card applies animated radius only when selected
- Drag enabled only after animations complete
- Wrap content toggles based on state

**4. Dynamic calculations:**
- Offset adjustment from height difference
- Drag bounds from content dimensions
- Conditional easing based on interaction type

**5. SharedTransitionScope integration:**
- Custom animations run alongside shared elements
- Same timing keeps everything synchronized
