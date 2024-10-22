package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaStorage.getMpaById(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
