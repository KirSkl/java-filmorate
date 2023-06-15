package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDao;

import java.util.Collection;

@Service
public class MpaService {

    MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Collection<MPARating> getAllRatings() {
        return mpaDao.getAllRatings();
    }

    public MPARating getMpaById(int id) {
        return mpaDao.getMpaById(id);
    }
}
