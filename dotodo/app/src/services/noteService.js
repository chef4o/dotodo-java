import * as request from "../lib/request";
const baseUrl = "http://localhost:3030/jsonstore/users";

export const getAllNotes = async (userID) => {
  const response = await request.get(`${baseUrl}/${userID}/notes`);

  return Object.values(response);
};

export const getSomeNotesByDueDateDesc = async (userId, numberOfResults) => {
  const response = await getAllNotes(userId);

  const sortedNotes = Object.values(response).sort((a, b) => new Date(b.dueDate) - new Date(a.dueDate));

  return sortedNotes.slice(-numberOfResults);
}

export const deleteNote = async (userID, id) => {
  const response = await request.remove(
    `${baseUrl}/${userID}/notes/${id}`
  );

  return response;
};

export const addNote = async (userID, body) => {
  const response = request.post(`${baseUrl}/${userID}/notes/`, body);

  return response;
};