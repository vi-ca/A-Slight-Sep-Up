import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import personReducer from "./personReducer";
import securityReducer from "./securityReducer";
import userReducer from "./userReducer";
import bookReducer from "./bookReducer"
import requestReducer from "./requestReducer";

export default combineReducers({
  errors: errorReducer,
  person: personReducer,
  security: securityReducer,
  user: userReducer,
  book: bookReducer,
  requests: requestReducer 
});

