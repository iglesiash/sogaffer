export const translatePosition = (f, position) => {
    return f({ id: `player.position.${position?.toLowerCase()}` });
} // translatePosition

export const translateBestFoot = (f, bestFoot) => {
    let translatedBestFoot = f({ id: `player.bestFoot.unknown` });
    if (bestFoot) {
        translatedBestFoot = f({ id: `player.bestFoot.${bestFoot?.toLowerCase()}` });
    } // if
    return translatedBestFoot;
} // translatedBestFoot

export const translatePlayingStatus = (f, status) => {
    let translatedPlayingStatus = f({ id: `player.playingStatus.unknown` });
    if (status) {
        const camelCaseStatus = status?.toLowerCase().replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
        translatedPlayingStatus = f({ id: `player.playingStatus.${camelCaseStatus}` });
    } // if
    return translatedPlayingStatus;
} // translatePlayingStatus

export const translateInjury = (f, injury) => {
    const camelCaseInjury = convertTextToCamelCase(injury);
    return f({ id: `player.injury.kind.${camelCaseInjury}` }).toLowerCase();
} // translateInjury

export const translateSuspension = (f, suspension) => {
    const camelCaseSuspension = convertTextToCamelCase(suspension);
    return f({ id: `player.suspension.reason.${camelCaseSuspension}` }).toLowerCase();
} // translateSuspension

const convertTextToCamelCase = (text) => {

    // Splits the string into an array of words
    const words = text.split(' ');

    // Capitalizes the first letter of each word except for the first word
    const camelCaseWords = words.map((word, index) => {
        if (index === 0) {
            return word.toLowerCase(); // Keep the first word in lowercase
        } else {
            return word.charAt(0).toUpperCase() + word.slice(1).toLowerCase();
        } // if-else
    }); // map

    // Joins the words back together
    return camelCaseWords.join('');
} // convertTextToCamelCase