const AI_SERVICE_URL = 'http://localhost:8094/api/ai';

export interface SpendingFromAI {
    amount: number;
    category: string;
    description: string;
    currency: string;
    spendDate: string;
}

export const aiService = {
    parseSpending: async (userInput: string): Promise<SpendingFromAI> => {
        const response = await fetch(`${AI_SERVICE_URL}/parse-spending`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userInput: userInput,
            }),
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`AI service error: ${errorText}`);
        }

        const data = await response.json();
        
        // Validate the response
        if (!data.amount || !data.category || !data.description || !data.currency || !data.spendDate) {
            throw new Error('Invalid response from AI service: missing required fields');
        }

        return {
            amount: Number(data.amount),
            category: data.category,
            description: data.description,
            currency: data.currency,
            spendDate: data.spendDate,
        };
    },

    transcribeAudio: async (audioBlob: Blob): Promise<string> => {
        const formData = new FormData();
        formData.append('audio', audioBlob, 'recording.webm');

        const response = await fetch(`${AI_SERVICE_URL}/transcribe-audio`, {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`AI service error: ${errorText}`);
        }

        const data = await response.json();
        
        if (!data.text) {
            throw new Error('Invalid response from AI service: missing transcription text');
        }

        return data.text;
    },

    parseSpendingFromAudio: async (audioBlob: Blob): Promise<SpendingFromAI> => {
        const formData = new FormData();
        formData.append('audio', audioBlob, 'recording.webm');

        const response = await fetch(`${AI_SERVICE_URL}/parse-spending-from-audio`, {
            method: 'POST',
            body: formData,
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`AI service error: ${errorText}`);
        }

        const data = await response.json();
        
        // Validate the response
        if (!data.amount || !data.category || !data.description || !data.currency || !data.spendDate) {
            throw new Error('Invalid response from AI service: missing required fields');
        }

        return {
            amount: Number(data.amount),
            category: data.category,
            description: data.description,
            currency: data.currency,
            spendDate: data.spendDate,
        };
    },
};

