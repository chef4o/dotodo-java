import * as request from "../lib/request";
const baseUrl = "http://localhost:8000/api/notes";

export const getAllNotes = async () => {
  const response = await request.get(baseUrl);

  return Object.values(response);
};

export const getSomeNotesByDueDateDesc = async (numberOfResults) => {
  const response = await getAllNotes();

  const sortedNotes = Object.values(response).sort((a, b) => new Date(b.dueDate) - new Date(a.dueDate));

  return sortedNotes.slice(-numberOfResults);
}

export const deleteNote = async (id) => {
  const response = await request.remove(
    `${baseUrl}/${id}`
  );

  return response;
};

export const addNote = async (body) => {
  const response = request.post(`${baseUrl}/notes/`, body);

  return response;
};