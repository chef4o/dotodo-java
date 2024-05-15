import * as request from "../lib/request";
const baseUrl = "http://localhost:3030/jsonstore/contacts";

export const getAllContacts = async () => {
  const response = await request.get(baseUrl);

  return Object.values(response);
};

export const findContactById = async (id) => {
  const response = await request.get(`${baseUrl}/${id}`);

  return response;
};

export const deleteContact = async (contactId) => {
  const response = await request.remove(`${baseUrl}/${contactId}`);

  return response;
};

export const createContact = async (body) => {
  const response = request.post(baseUrl, body);

  return response;
};