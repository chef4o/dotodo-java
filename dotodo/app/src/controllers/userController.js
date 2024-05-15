import { isEmail } from "validator";
import { validate } from "uuid";
import {
  findUserByEmail,
  findUserById,
  findUserByUsername,
  getAllUsers,
} from "../services/userService";
import { v4 as uuidv4 } from "uuid";

/*
The getUser function checks the type of the param and acts as follows: 
- when the param is uuid    then searches by _id;
- when the param is email   then searches by email;
- when the param is string  then searches by username; 
- when there is no match    then returns undefined; 
*/
export const getUser = async (param) => {
  const user = validate(param)
    ? findUserById(param)
    : isEmail(param)
    ? findUserByEmail(param)
    : findUserByUsername(param);

  return user;
};

export const isPasswordValid = (userPass, password) => {
  return userPass === password;
};

export const getFreeUuid = async (ids, id) => {
  const userIds = ids.length ? ids : Object.keys(await getAllUsers());

  let uuId = id || uuidv4();

  if (userIds.includes(uuId)) {
    uuId = uuidv4();
    return await getFreeUuid(userIds, uuId);
  } else {
    return uuId;
  }
};
