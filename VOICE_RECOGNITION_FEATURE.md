# 🎤 Voice Recognition Feature for AI Spending

## Overview

This feature adds voice recognition capability to the AI Spending form using the browser's built-in **Web Speech API** for real-time speech-to-text conversion.

## Evolution of the Solution

### Initial Challenge

The first implementation attempted to use a Whisper API endpoint (`https://autotests.ai/ollama/v1/audio/transcriptions`) for backend audio transcription, which resulted in a **405 Method Not Allowed** error as this endpoint doesn't exist on the autotests.ai server.

### Final Solution

We switched to using the browser's built-in **Web Speech API** instead of backend audio transcription, which proved to be a superior solution in every way.

### ✅ Advantages of Web Speech API Approach

1. **No Backend Dependency**: Voice recognition happens entirely in the browser
2. **Real-Time Transcription**: See your words appear as you speak! 🎉
3. **Faster**: No audio file upload or processing delay
4. **Works Offline**: After initial page load, voice recognition works without internet
5. **No API Costs**: Uses browser's free built-in capabilities
6. **Better Privacy**: Audio never leaves your device
7. **No Storage**: No need to handle audio file uploads or temporary storage
8. **Instant Feedback**: Users can verify transcription accuracy before processing

## What's New

### Backend Changes

No backend changes required! The voice recognition uses browser's Web Speech API, which processes speech entirely on the client side. The backend continues to use the existing `/api/ai/parse-spending` endpoint to parse the transcribed text.

**Note**: The `WhisperService.java` that was initially created is not needed and can be removed if desired.

### Frontend Changes

1. **New Component: `VoiceRecorder`**
   - Located: `niffler-ng-client/src/components/VoiceRecorder/index.tsx`
   - Features:
     - Animated recording button with pulse effect
     - Real-time transcription display (see text as you speak!)
     - Recording timer
     - Microphone permission handling
     - Error handling and user feedback
     - Uses Web Speech API for instant speech recognition

2. **Updated Component: `AISpendingForm`**
   - Added tab-based interface for Text/Voice input modes
   - Integrated VoiceRecorder component
   - Displays transcribed text for user verification
   - Automatic processing after recording stops

3. **Updated Service: `aiService.ts`**
   - Uses existing `parseSpending()` method with transcribed text
   - No audio file upload needed

## How to Use

### For Users

1. Open the AI Spending page (click the AI Spending button with ✨ icon)
2. Click on the **"Voice Input"** tab
3. Click the blue microphone button
4. Grant microphone permissions if prompted
5. Speak your spending description (e.g., "Купил кофе за 300 рублей")
6. **Watch your words appear in real-time as you speak!** ✨
7. Click the red stop button
8. Review the transcribed text and parsed spending information
9. Click "Save" to add the spending

### Example Flow

```
You say: "Купил кофе за 300 рублей"
         ↓
Browser transcribes in real-time: [text appears as you speak]
         ↓
Text sent to backend: "Купил кофе за 300 рублей"
         ↓
AI parses: amount=300, category="Рестораны", currency="RUB"
         ↓
Done! ✅
```

### For Developers

#### Testing the Feature

Since voice recognition uses the browser's Web Speech API, testing is done directly in the browser:
1. **Refresh your browser** to load the updated code
2. Open the app in Chrome, Edge, or Safari (not Firefox)
3. Navigate to AI Spending page
4. Click Voice Input tab
5. Test with your microphone
6. The microphone icon should turn red with a pulse animation when recording
7. You'll see a live transcription box appear as you speak
8. The feature works best with clear speech and minimal background noise

#### Running Locally

1. Start the AI service:
   ```bash
   ./gradlew :niffler-ai:bootRun
   ```

2. Start the frontend:
   ```bash
   cd niffler-ng-client
   npm run dev
   ```

3. Open http://localhost:3000 and navigate to AI Spending

## Architecture

```
User speaks into microphone
    ↓
Browser Web Speech API recognizes speech in real-time
    ↓
VoiceRecorder displays transcription as user speaks
    ↓
User stops recording
    ↓
Transcribed text sent to backend via aiService.parseSpending()
    ↓
OllamaService parses text → spending data
    ↓
Response sent back to frontend
    ↓
AISpendingForm displays results
```

## Technical Details

### Technology Stack

- **API**: Web Speech API (built into modern browsers)
- **Recognition**: Real-time, no file upload needed
- **Processing**: Happens entirely in the browser
- **Backend**: Existing `/api/ai/parse-spending` endpoint with Ollama

### Technical Changes Summary

#### Frontend (`VoiceRecorder` component)
- **Before**: Recorded audio using MediaRecorder → Sent WebM file to backend
- **After**: Uses Web Speech API → Gets text directly in browser

#### Backend
- **No changes needed!** Continues to use existing `/api/ai/parse-spending` endpoint
- The `WhisperService.java` is not needed and can be removed

### Supported Languages

- Russian (Русский) - primary language
- English - auto-detected
- Any other language supported by the browser's speech recognition

### Browser Compatibility

| Browser | Support | Notes |
|---------|---------|-------|
| Chrome/Chromium | ✅ Full | Best experience |
| Microsoft Edge | ✅ Full | Same as Chrome |
| Safari | ✅ Full | iOS 14.3+, macOS |
| Firefox | ❌ No | Web Speech API not available |
| Opera | ✅ Full | Chromium-based |

### Error Handling

- Microphone permission denied → User-friendly error message
- No speech detected → Error message with retry option
- Browser not supported → Clear message to use Chrome/Edge/Safari
- Network errors (during parsing) → Graceful error handling with user feedback

## File Structure

```
niffler-ai/
├── src/main/java/guru/qa/niffler/
│   ├── controller/
│   │   └── AiController.java (unchanged - uses existing endpoint)
│   ├── service/
│   │   ├── OllamaService.java (existing)
│   │   └── WhisperService.java (not needed, can be removed)
│   └── model/
│       └── ... (existing models)
└── src/main/resources/
    └── application.yaml (unchanged)

niffler-ng-client/
├── src/
│   ├── api/
│   │   └── aiService.ts (updated)
│   └── components/
│       ├── AISpendingForm/
│       │   └── index.tsx (updated)
│       └── VoiceRecorder/
│           └── index.tsx (new - uses Web Speech API)
```

## API Reference

The voice recognition feature uses the existing text parsing endpoint:

### POST /api/ai/parse-spending

**Request:**
```json
{
  "userInput": "Купил кофе за 300 рублей"
}
```

**Response:**
```json
{
  "amount": 300.0,
  "category": "Рестораны",
  "description": "Купил кофе",
  "currency": "RUB",
  "spendDate": "2025-10-21"
}
```

## Security Considerations

1. **API Token**: Stored securely on backend, not exposed to client
2. **No File Upload**: Audio is not recorded or uploaded, only text is sent
3. **Privacy**: Speech processing happens entirely in the browser
4. **No Storage**: Audio never leaves the user's device
5. **CORS**: Configured to allow requests only from trusted origins
6. **Microphone Access**: Requires explicit user permission

## Future Improvements

1. ✅ Real-time transcription display (already implemented!)
2. Add language selector (Russian/English/Auto)
3. Add confidence score display
4. Support for voice commands ("save", "cancel", etc.)
5. Add audio waveform visualization
6. Add grammar hints for better recognition
7. Support for multiple currency names in voice
8. Add dictation punctuation ("comma", "period", etc.)
9. Add keyboard shortcuts for start/stop recording
10. Support for continuous recording mode

## Troubleshooting

### "Microphone permission denied"
- Check browser permissions (Settings → Privacy → Microphone)
- Ensure HTTPS or localhost (required for microphone access)
- On macOS: Check System Preferences → Security & Privacy → Microphone

### "Your browser doesn't support voice recognition"
- Switch to Chrome, Edge, or Safari
- Update your browser to the latest version
- Firefox users: This feature requires Chrome-based browsers or Safari

### No speech detected
- Check system microphone is working
- Verify browser has microphone permission
- Speak clearly and closer to the microphone
- Check that microphone is not muted in system settings
- Try a different microphone if available

### Transcription in wrong language
- The API is set to Russian by default
- It should auto-detect English as well
- Speak clearly for better recognition
- Consider adding a language selector (future improvement)

### Text appears but doesn't get parsed
- Check that the backend AI service is running
- Verify network connection
- Check browser console for errors
- Ensure Ollama service is accessible

---

**Status**: ✅ Ready to use!  
**Issue**: Fixed (405 error resolved with Web Speech API solution)  
**Author:** AI Assistant  
**Date:** October 21-24, 2025  
**Version:** 2.0.0 (Updated with Web Speech API)
