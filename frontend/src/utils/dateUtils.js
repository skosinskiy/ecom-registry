export const getDateTimeString = timestamp => {
  const date = new Date(timestamp)
  return `${getDateString(date)} 
  ${appendZero(date.getHours())}:${appendZero(date.getMinutes())}:${appendZero(date.getSeconds())}`
}

export const getDateString = date =>
  `${date.getFullYear()}-${appendZero(date.getMonth() + 1)}-${appendZero(date.getDate())}`

const appendZero = date => date.toString().length === 1 ? '0' + date : date
