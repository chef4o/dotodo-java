import * as request from "../lib/request";
const baseUrl = "http://localhost:8000/api/users";

export const getAllUsers = async () => {
  const response = await request.get(baseUrl);

  return Object.values(response);
};

export const findUserById = async (id) => {
  const response = await request.get(`${baseUrl}/${id}`);

  return response;
};

export const findUserByEmail = async (email) => {
  const user = await getAllUsers();
  return user.find((user) => user.email === email);
};

export const findUserByUsername = async (username) => {
  const user = await getAllUsers();

  return user.find((user) => user.username === username);
};

export const deleteUser = async (userId) => {
  const response = await request.remove(`${baseUrl}/${userId}`);

  return response;
};

export const addUser = async (body) => {
  const response = request.post(baseUrl, body);

  return response;
};

export const editUser = async (body) => {
  const response = request.put(baseUrl, body);

  return response;
};

export const userExists = async (username, email) => {
  const existingUsername = await findUserByUsername(username);
  const existingEmail = await findUserByEmail(email);

  return {
    withUsername: !!existingUsername,
    withEmail: !!existingEmail,
  };
};
