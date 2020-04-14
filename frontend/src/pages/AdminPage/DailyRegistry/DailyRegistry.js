import React, { useState } from 'react'
import { DailyRegistryHeader } from './DailyRegistryHeader/DailyRegistryHeader'
import { DailyRegistryTable } from './DailyRegistryTable/DailyRegistryTable'

export const DailyRegistry = () => {
  const [date, setDate] = useState(new Date())

  return (
    <>
      <DailyRegistryHeader date={date} setDate={setDate} />
      <DailyRegistryTable date={date} />
    </>
  )
}