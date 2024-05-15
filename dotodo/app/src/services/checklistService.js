import * as request from "../lib/request";
const baseUrl = "http://localhost:3030/jsonstore/users";

export const getAllChecklists = async (userID) => {
  const response = await request.get(`${baseUrl}/${userID}/checklists`);

  return Object.values(response);
};

export const getCheckListByUserAndId = async (userID, id) => {
  const response = await request.get(`${baseUrl}/${userID}/checklists/${id}`);

  return Object.values(response);
};

export const deleteChecklist = async (userID, id) => {
  const response = await request.remove(
    `${baseUrl}/${userID}/checklists/${id}`
  );

  return response;
};

export const addChecklist = async (userID, body) => {
  const response = request.post(`${baseUrl}/${userID}/checklists/`, body);

  return response;
};

export const editChecklist = async (userID, body) => {
  const response = request.put(`${baseUrl}/${userID}/checklists/`, body);

  return response;
};

export const getCheckListData = async (userID, checklistId, elementId) => {
  const response = await request.get(
    `${baseUrl}/${userID}/checklists/${checklistId}/elements/${elementId}`
  );

  return response;
};

export const editChecklistItem = async (
  userID,
  checklistId,
  elementId,
  body
) => {
  const response = await request.put(
    `${baseUrl}/${userID}/checklists/${checklistId}/elements/${elementId}`,
    body
  );

  return response;
};

export const addChecklistItem = async (
  userID,
  checklistId,
  elementId,
  body
) => {
  const response = request.post(
    `${baseUrl}/${userID}/checklists/${checklistId}/elements/${elementId}`,
    body
  );

  return response;
};

export const deleteChecklistItem = async (userID, checklistId, elementId) => {
  const response = await request.remove(
    `${baseUrl}/${userID}/checklists/${checklistId}/elements/${elementId}`
  );

  return response;
};

export const getSomeChecklistsByDueDateDesc = async (
  userId,
  numberOfResults
) => {
  const response = await getAllChecklists(userId);

  return response.slice(-numberOfResults);
};
