import React from 'react'
import ReduxToastr from 'react-redux-toastr'

const ToastrMessage = props => (
  <ReduxToastr
    timeOut={5000}
    newestOnTop={true}
    preventDuplicates
    position="bottom-left"
    transitionIn="fadeIn"
    transitionOut="fadeOut"
    progressBar
    closeOnToastrClick
  />
)

export default ToastrMessage