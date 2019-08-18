import * as TYPES from './types'

const initialState = {
    currentUser: null,
    isCurrentUserLoading: true
}

const usersReducer = (state = initialState, action) => {

    switch (action.type) {
        case TYPES.CURRENT_USER_FETCHED:
            return {
                ...state,
                currentUser: action.payload
            }
        case TYPES.CURRENT_USER_LOADING:
            return {
                ...state,
                isCurrentUserLoading: action.payload
            }
        default:
            return {...state}
    }
}

export default usersReducer