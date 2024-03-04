
// TODO: get locale
export const formatDate = (date, locale, options) => {
    return new Intl.DateTimeFormat(locale, options).format(new Date(date));
}

export const showPlayerBirthInformation = (birthDate, age) => {
    return formatDate(birthDate, 'es') + " (" + age + " años)";
}

export const formatExpectedInjuryEndDate = (date) => {
    let formattedDate = 'desconocida';
    if (date) {
        formattedDate = formatDate(date, 'es');
    }

    return formattedDate;
}